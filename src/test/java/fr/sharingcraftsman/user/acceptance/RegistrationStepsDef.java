package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationStepsDef extends SpringAcceptanceTestConfig {
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
    LoginDsl login = new LoginDsl(email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/tokens/login")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @Then("I am connected")
  public void checkTokenPresent() throws IOException {
    String tokenString = response.getResponse().getContentAsString();
    OAuthToken token = Mapper.fromJsonStringToObject(tokenString, OAuthToken.class);
    assertThat(token.getAccessToken()).isNotEmpty();
    assertThat(token.getRefreshToken()).isNotEmpty();
  }
}
