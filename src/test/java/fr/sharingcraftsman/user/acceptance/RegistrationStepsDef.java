package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

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

  @Given("I register to the application with my credentials <(.*)> and password <(.*)>")
  public void register(String email, String password) throws Exception {
    LoginDsl login = new LoginDsl(email, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("I connect to the application with my credentials <(.*)> and password <(.*)>")
  public void connect(String email, String password) {
//    LoginDsl login = new LoginDsl(email, password);
//
//    response = given()
//            .contentType("application/json")
//            .body(login)
//            .when()
//            .post("/user/connect");
//
//    assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Then("I am connected")
  public void checkTokenPresent() throws UnsupportedEncodingException {

//    TokenDsl token = response.as(TokenDsl.class);
//
//    assertThat(token.getAccessToken()).isNotEmpty();
//    assertThat(token.getRefreshToken()).isNotEmpty();
    System.out.println(response.getResponse().getContentAsString());
  }


}
