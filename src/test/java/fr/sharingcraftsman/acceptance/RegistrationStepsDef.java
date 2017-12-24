package fr.sharingcraftsman.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.acceptance.dsl.TokenDsl;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.context.embedded.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationStepsDef extends SpringAcceptanceTestConfig {
  private Response response;
//
//  @LocalServerPort
//  int port;

  @Given("The application is setup")
  public void setupApplication() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8080;
  }

  @Given("I register to the application with my credentials <(.*)> and password <(.*)>")
  public void register(String email, String password) {
    LoginDsl login = new LoginDsl(email, password);

    given()
            .contentType("application/json")
            .body(login)
            .when()
            .post("/user/register")
            .then()
            .statusCode(200);
  }

  @When("I connect to the application with my credentials <(.*)> and password <(.*)>")
  public void connect(String email, String password) {
    LoginDsl login = new LoginDsl(email, password);

    response = given()
            .contentType("application/json")
            .body(login)
            .when()
            .post("/user/connect");

    assertThat(response.getStatusCode()).isEqualTo(200);
  }

  @Then("I am connected")
  public void checkTokenPresent() {
    TokenDsl token = response.as(TokenDsl.class);

    assertThat(token.getAccessToken()).isNotEmpty();
    assertThat(token.getRefreshToken()).isNotEmpty();
  }
}
