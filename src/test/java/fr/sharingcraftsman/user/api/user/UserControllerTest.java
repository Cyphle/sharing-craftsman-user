package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.authentication.LoginDTO;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.utils.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(UserController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private UserService userService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_register_a_new_user() throws Exception {
    given(userService.registerUser(any(ClientDTO.class), any(LoginDTO.class))).willReturn(ResponseEntity.ok().build());

    LoginDTO loginDTO = LoginDTO.from("john@doe.fr", "password");

    this.mvc.perform(post("/users/register")
            .header("client", "client")
            .header("secret", "clientsecret")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(loginDTO)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_request_for_a_token_to_change_password() throws Exception {
    given(userService.requestChangePassword(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(get("/users/request-change-password")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_change_password() throws Exception {
    given(userService.changePassword(any(ClientDTO.class), any(TokenDTO.class), any(ChangePasswordDTO.class))).willReturn(ResponseEntity.ok().build());

    ChangePasswordDTO changePassword = new ChangePasswordDTO();
    changePassword.setOldPassword("password");
    changePassword.setNewPassword("newpassword");
    this.mvc.perform(post("/users/change-password")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(changePassword)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_update_profile() throws Exception {
    ProfileDTO profile = new ProfileDTO("John", "Doe", "john@doe.fr", "www.johndoe.fr", "http://github.com/Johndoe", "linkedin.com/johndoe");
    given(userService.updateProfile(any(ClientDTO.class), any(TokenDTO.class), any(ProfileDTO.class))).willReturn(ResponseEntity.ok(profile));

    this.mvc.perform(post("/users/change-password")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(profile)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_generate_token_when_lost_password() throws Exception {
    given(userService.requestChangePassword(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(get("/users/lost-password")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr"))
            .andExpect(status().isOk());
  }
}
