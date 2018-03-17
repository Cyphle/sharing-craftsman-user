package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
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
  private AuthorizationAdminService authorizationAdminService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_get_groups() throws Exception {
    GroupDTO groupUser = GroupDTO.from("USERS");
    groupUser.addRoles(Collections.singletonList(RoleDTO.from("ROLE_USER")));
    GroupDTO groupAdmin = GroupDTO.from("ADMINS");
    groupAdmin.addRoles(Arrays.asList(RoleDTO.from("ROLE_USER"), RoleDTO.from("ROLE_ADMIN")));
    List<GroupDTO> groups = Arrays.asList(groupUser, groupAdmin);
    given(authorizationAdminService.getGroups(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok(groups));

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
    roles.add(RoleDTO.from("ROLE_ROOT"));
    roles.add(RoleDTO.from("ROLE_ADMIN"));
    roles.add(RoleDTO.from("ROLE_USER"));
    GroupDTO newGroup = GroupDTO.from("SUPER_ADMINS", roles);

    this.mvc.perform(post("/admin/roles/groups")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(newGroup)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(RoleDTO.from("ROLE_USER"));
    GroupDTO newGroup = GroupDTO.from("SUPER_ADMINS", roles);

    this.mvc.perform(post("/admin/roles/groups/delete")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(newGroup)))
            .andExpect(status().isOk());
  }
}
