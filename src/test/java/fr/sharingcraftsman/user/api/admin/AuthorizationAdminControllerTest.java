package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.utils.Mapper;
import org.assertj.core.util.Sets;
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

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(AuthorizationAdminController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthorizationAdminControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AdminService adminService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_get_groups() throws Exception {
    GroupDTO groupUser = new GroupDTO("USERS");
    groupUser.addRoles(Collections.singletonList(new RoleDTO("ROLE_USER")));
    GroupDTO groupAdmin = new GroupDTO("ADMINS");
    groupAdmin.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    List<GroupDTO> groups = Arrays.asList(groupUser, groupAdmin);
    given(adminService.getGroups(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok(groups));

    this.mvc.perform(get("/admin/roles/groups")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_add_new_group_with_roles() throws Exception {
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(new RoleDTO("ROLE_ROOT"));
    roles.add(new RoleDTO("ROLE_ADMIN"));
    roles.add(new RoleDTO("ROLE_USER"));
    GroupDTO newGroup = new GroupDTO("SUPER_ADMINS", roles);

    this.mvc.perform(post("/admin/roles/groups")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(newGroup)))
            .andExpect(status().isOk());
  }

  /*
  - Get list of users with profiles -> OK
  - Remove user -> OK
  - Deactivate user + Modify user (send all user except authorizations) -> OK
  - Modify user ? -> OK
  - Add user -> OK
  - Get list of roles, groups -> OK
  - Add/remove roles groups (impact on user groups) -> OK
  - Get list of authorizations -> OK
  - Add/remove authorizations
   */
}
