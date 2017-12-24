package fr.sharingcraftsman.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.acceptance.dsl.TokenDsl;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationStepsDef extends SpringAcceptanceTestConfig {
  private MockMvc mvc;

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

//    given()
//            .contentType("application/json")
//            .body(login)
//            .when()
//            .post("/user/register")
//            .then()
//            .statusCode(200);


//    MvcResult mvcResult = this.mvc
//            .perform(MockMvcRequestBuilders.get("http://localhost:8080/version")).andReturn();

    ObjectMapper mapper = new ObjectMapper();
    String jsonInString = mapper.writeValueAsString(login);

    ResultActions resultActions = this.mvc.perform(post(getBaseUri() + "/user/test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonInString));

    String test = "toto";
//
//    String version = mvcResult.getResponse().getContentAsString();
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
  public void checkTokenPresent() {
//    TokenDsl token = response.as(TokenDsl.class);
//
//    assertThat(token.getAccessToken()).isNotEmpty();
//    assertThat(token.getRefreshToken()).isNotEmpty();
  }


}
