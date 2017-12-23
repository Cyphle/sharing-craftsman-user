package fr.sharingcraftsman.acceptance;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class MyServiceSteps extends SpringIntegrationTest {
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  private MyService service;
  private boolean success;

  private String ADMIN_USER = "user";
  private String PASSWORD = "world";
  private String version;


  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
  }

  @Given("I go on the application")
  public void initializeApplication() {
    if (this.mvc == null) {
      this.mvc = MockMvcBuilders
              .webAppContextSetup(context)
              .apply(springSecurity())
              .build();
    }
  }

  @Given("^my service exists$")
  public void my_service_exists() throws Throwable {
    MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/version")).andReturn();

    version = mvcResult.getResponse().getContentAsString();
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
