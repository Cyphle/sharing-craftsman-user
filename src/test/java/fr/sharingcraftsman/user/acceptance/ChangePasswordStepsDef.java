package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChangePasswordStepsDef extends SpringAcceptanceTestConfig {
  @When("I ask to change my password")
  public void requestPasswordChange() throws Exception {
//    response = this.mvc
//            .perform(get(getBaseUri() + "/users/request-password-change")
//                    .header("client", "sharingcraftsman")
//                    .header("secret", "secret")
//                    .header("username", login.getUsername()))
//                    .header("access-token", token.getAccessToken())
//            )
//            .andExpect(status().isOk())
//            .andReturn();
  }

  @And("I change my password with new password <(.*)>")
  public void changePassword(String newPassword) {
//    response = this.mvc
//            .perform(post(getBaseUri() + "/users/change-password")
//                    .header("client", "sharingcraftsman")
//                    .header("secret", "secret")
//                    .header("username", login.getUsername()))
//                    .header("access-token", token.getAccessToken())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(Mapper.fromObjectToJsonString(password))
//            )
//            .andExpect(status().isOk())
//            .andReturn();
  }
}
