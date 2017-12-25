package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class MyServiceSteps extends SpringAcceptanceTestConfig {
  private String version;

  @Given("I setup application")
  public void setUp() throws Exception {
//    RestAssured.baseURI = "http://localhost";
//    RestAssured.port = 8080;
  }

  @Given("^my service exists$")
  public void my_service_exists() throws Throwable {
//    Response response = given().get("/version");
//    String toto = "toto";
  }

  @When("^I call my service$")
  public void i_call_my_service() throws Throwable {
//    success = service.doSomething();
  }

  @Then("^it should have been a success$")
  public void it_should_have_been_a_success() throws Throwable {
//    assertThat(success).isTrue();
    assertThat(version).isEqualTo("1.0");
  }
}
