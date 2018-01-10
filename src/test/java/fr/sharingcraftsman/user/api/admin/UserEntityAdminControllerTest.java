package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
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

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(UserAdminController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserEntityAdminControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AdminService adminService;

  private UserInfoDTO userDTO;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    userDTO = new UserInfoDTO("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
  }

  @Test
  public void should_get_list_of_users_with_their_profile() throws Exception {
    given(adminService.getAllUsers(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok(Collections.singletonList(userDTO)));

    this.mvc.perform(get("/admin/users")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk())
            .andReturn();
  }

  @Test
  public void should_delete_user() throws Exception {
    given(adminService.deleteUser(any(ClientDTO.class), any(TokenDTO.class), any(String.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(delete("/admin/users/hello@world.fr")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_update_a_user() throws Exception {
    given(adminService.updateUser(any(ClientDTO.class), any(TokenDTO.class), any(UserInfoDTO.class))).willReturn(ResponseEntity.ok(userDTO));

    this.mvc.perform(put("/admin/users")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(userDTO)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_add_user() throws Exception {
    given(adminService.updateUser(any(ClientDTO.class), any(TokenDTO.class), any(UserInfoDTO.class))).willReturn(ResponseEntity.ok(userDTO));

    this.mvc.perform(post("/admin/users")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(userDTO)))
            .andExpect(status().isOk());
  }
}
