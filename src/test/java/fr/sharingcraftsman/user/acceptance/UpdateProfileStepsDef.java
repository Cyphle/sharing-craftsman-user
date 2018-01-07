package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.LoginDsl;
import fr.sharingcraftsman.user.acceptance.dsl.ProfileDsl;
import fr.sharingcraftsman.user.acceptance.dsl.TokenDsl;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateProfileStepsDef extends SpringAcceptanceTestConfig {
  private ProfileDsl profileDsl;
  private ProfileDsl newProfile;

  @Before
  public void setUp() throws Exception {
    if (this.mvc == null) {
      this.mvc = MockMvcBuilders
              .webAppContextSetup(context)
              .build();
    }

    ClientDTO client = new ClientDTO();
    client.setName("sharingcraftsman");
    this.mvc
            .perform(post(getBaseUri() + "/clients/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(client))
            )
            .andExpect(status().isOk());
  }

  @Given("^I have a connected account with credentials <(.*)> and <(.*)>$")
  public void registerAndConnect(String username, String password) throws Exception {
    login = new LoginDsl(username, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/users/register")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();

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

  @When("^I update my account with firstname <(.*)>, lastname <(.*)>, email <(.*)>, website <(.*)>, github <(.*)>, linkedin <(.*)>$")
  public void updateProfile(String firstname, String lastname, String email, String website, String github, String linkedin) throws Exception {
    profileDsl = new ProfileDsl(firstname, lastname, email, website, github, linkedin);

    response = this.mvc
            .perform(post(getBaseUri() + "/users/update-profile")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(profileDsl))
            )
            .andExpect(status().isOk())
            .andReturn();

    newProfile = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), ProfileDsl.class);
  }

  @Then("^I have my profile updated$")
  public void checkNewToken() throws IOException {
    assertThat(profileDsl).isEqualTo(newProfile);
  }
}
