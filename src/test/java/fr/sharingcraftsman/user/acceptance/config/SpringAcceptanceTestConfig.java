package fr.sharingcraftsman.user.acceptance.config;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(
        classes = {UserApplication.class},
        loader = SpringBootContextLoader.class)
@WebAppConfiguration
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class SpringAcceptanceTestConfig {
  private final String BASE_URI = "http://localhost";
  private final int PORT = 8080;

  protected MockMvc mvc;
  protected MvcResult response;

  @Autowired
  protected WebApplicationContext context;

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
    OAuthClient client = new OAuthClient();
    client.setName(clientName);

    this.mvc
            .perform(post(getBaseUri() + "/clients/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(client))
            )
            .andExpect(status().isOk());
  }

  protected String getBaseUri() {
    return BASE_URI + ":" + PORT;
  }
}
