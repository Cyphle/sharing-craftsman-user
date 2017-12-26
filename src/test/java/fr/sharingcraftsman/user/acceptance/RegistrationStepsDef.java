package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.user.api.models.ClientRegistration;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationStepsDef extends SpringAcceptanceTestConfig {
  private MockMvc mvc;
  private MvcResult response;

  @Autowired
  private WebApplicationContext context;

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
    ClientRegistration client = new ClientRegistration();
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
    LoginDsl login = new LoginDsl("sharingcraftsman", "secret", email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("I connect to the application with my credentials <(.*)> and password <(.*)>")
  public void connect(String email, String password) throws Exception {
    LoginDsl login = new LoginDsl("sharingcraftsman", "secret", email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/users/login")
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