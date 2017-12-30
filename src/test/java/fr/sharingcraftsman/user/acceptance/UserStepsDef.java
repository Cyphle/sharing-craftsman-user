package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.*;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserStepsDef extends SpringAcceptanceTestConfig {
  private TokenDsl newToken;
  private AuthorizationDsl authorization;

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
            )
            .andExpect(status().isOk());
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

  @And("I refresh my token")
  public void refreshToken() throws Exception {
    response = this.mvc
            .perform(get(getBaseUri() + "/tokens/refresh-token")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("refresh-token", token.getRefreshToken())
            )
            .andExpect(status().isOk())
            .andReturn();

    newToken = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), TokenDsl.class);
  }

  @And("I ask for my groups")
  public void getAuthorizations() throws Exception {
    response = this.mvc
            .perform(get(getBaseUri() + "/roles")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
            )
            .andExpect(status().isOk())
            .andReturn();

    authorization = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), AuthorizationDsl.class);
  }

  @And("I have the group <(.*)>")
  public void verifyGroup(String group) {
    if (group.equals("USERS")) {
      List<String> groupNames = authorization.getGroups()
              .stream()
              .map(GroupDsl::getName)
              .collect(Collectors.toList());
      assertThat(groupNames).contains(group);
    }
  }

  @Then("I have a new token")
  public void checkNewToken() throws IOException {
    assertThat(newToken.getAccessToken()).isNotEmpty();
    assertThat(newToken.getRefreshToken()).isNotEmpty();
    assertThat(newToken.getAccessToken()).isNotEqualTo(token.getAccessToken());
    assertThat(newToken.getRefreshToken()).isNotEqualTo(token.getRefreshToken());
  }

  @Then("I am connected")
  public void checkTokenPresent() throws IOException {
    assertThat(token.getAccessToken()).isNotEmpty();
    assertThat(token.getRefreshToken()).isNotEmpty();
  }
}
