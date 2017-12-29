package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.ChangePasswordDsl;
import fr.sharingcraftsman.user.acceptance.dsl.ChangePasswordTokenDsl;
import fr.sharingcraftsman.user.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.user.acceptance.dsl.TokenDsl;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserStepsDef extends SpringAcceptanceTestConfig {
  @Given("The application is setup")
  public void setupApplication() {
    if (this.mvc == null) {
      this.mvc = MockMvcBuilders
              .webAppContextSetup(context)
              .build();
    }
  }

  @And("A client <(.*)> is registered")
  public void createClient(String clientName) throws Exception {
    ClientDTO client = new ClientDTO();
    client.setName(clientName);

    this.mvc
            .perform(post(getBaseUri() + "/clients/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(client))
            );
  }

  @Given("I register to the application with my credentials <(.*)> and password <(.*)>")
  public void register(String email, String password) throws Exception {
    LoginDsl login = new LoginDsl(email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/users/register")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("I connect to the application with my credentials <(.*)> and password <(.*)>")
  public void connect(String email, String password) throws Exception {
    login = new LoginDsl(email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/tokens/login")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();

    token = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), TokenDsl.class);
  }

  @When("I ask to change my password")
  public void requestPasswordChange() throws Exception {
    response = this.mvc
            .perform(get(getBaseUri() + "/users/request-change-password")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
            )
            .andExpect(status().isOk())
            .andReturn();

    changePasswordToken = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), ChangePasswordTokenDsl.class);
  }

  @And("I change my password with new password <(.*)>")
  public void changePassword(String newPassword) throws Exception {
    ChangePasswordDsl changePasswordDsl = new ChangePasswordDsl(changePasswordToken.getToken(), login.getPassword(), newPassword);

    response = this.mvc
            .perform(post(getBaseUri() + "/users/change-password")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(changePasswordDsl))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @Then("I am connected")
  public void checkTokenPresent() throws IOException {
    String tokenString = response.getResponse().getContentAsString();
    TokenDTO token = Mapper.fromJsonStringToObject(tokenString, TokenDTO.class);
    assertThat(token.getAccessToken()).isNotEmpty();
    assertThat(token.getRefreshToken()).isNotEmpty();
  }
}
