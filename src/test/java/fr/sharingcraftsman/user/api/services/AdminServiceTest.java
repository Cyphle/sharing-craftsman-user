package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.admin.HRAdminManager;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
  @Mock
  private HRAdminManager hrAdminManager;
  @Mock
  private GroupAdministrator groupAdministrator;
  @Mock
  private RoleAdministrator roleAdministrator;
  @Mock
  private ClientStock clientStock;
  @Mock
  private DateService dateService;

  private AdminService adminService;

  private ClientDTO clientDTO;
  private ValidToken validToken;
  private TokenDTO tokenDTO;
  private AdminUserDTO user;
  private AdminUserDTO adminUser;

  @Before
  public void setUp() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));
    given(clientStock.findClient(any(Client.class))).willReturn(Client.knownClient("client", "secret"));

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    user = new AdminUserDTO("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    user.setPassword("password");

    GroupDTO adminGroup = new GroupDTO("ADMINS");
    adminGroup.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    AuthorizationsDTO adminAuthorization = new AuthorizationsDTO();
    adminAuthorization.addGroup(adminGroup);
    adminUser = new AdminUserDTO("admin@toto.fr", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", adminAuthorization, true, 1514631600000L, 1514631600000L);
    adminUser.setPassword("password");

    clientDTO = new ClientDTO("client", "secret");
    validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();

    tokenDTO = new TokenDTO();
    tokenDTO.setUsername("admin@toto.fr");
    tokenDTO.setAccessToken("aaa");

    adminService = new AdminService(hrAdminManager, clientStock, groupAdministrator, roleAdministrator);
  }

  @Test
  public void should_get_list_of_users_with_profile_and_authorizations() throws Exception {
    List<AdminCollaborator> collaborators = Arrays.asList(
            AdminCollaborator.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "", null, true, new Date(), new Date()),
            AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date())
    );
    given(hrAdminManager.getAllCollaborators()).willReturn(collaborators);
    given(groupAdministrator.findGroupsOf(usernameBuilder.from("john@doe.fr"))).willReturn(Collections.singletonList(new Group("USERS")));
    given(roleAdministrator.getRolesOf("USERS")).willReturn(Collections.singletonList(new Role("ROLE_USER")));
    given(groupAdministrator.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(new Group("ADMINS")));
    given(roleAdministrator.getRolesOf("ADMINS")).willReturn(Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    verify(hrAdminManager).getAllCollaborators();
    assertThat(response.getBody()).isEqualTo(Arrays.asList(user, adminUser));
  }

  @Test
  public void should_get_unauthorized_if_client_is_not_known() throws Exception {
    given(clientStock.findClient(any(Client.class))).willReturn(Client.unkownClient());

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_get_unauthorized_if_requester_has_not_role_admin() throws Exception {
    given(groupAdministrator.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(new Group("USERS")));
    given(roleAdministrator.getRolesOf("USERS")).willReturn(Collections.singletonList(new Role("ROLE_USER")));

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_delete_user() throws Exception {
    given(groupAdministrator.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(new Group("ADMINS")));
    given(roleAdministrator.getRolesOf("ADMINS")).willReturn(Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
    Mockito.doNothing().when(hrAdminManager).deleteCollaborator(any(Username.class));

    ResponseEntity response = adminService.deleteUser(clientDTO, tokenDTO, "hello@world.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(hrAdminManager).deleteCollaborator(usernameBuilder.from("hello@world.fr"));
  }

  @Test
  public void should_update_user() throws Exception {
    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    AdminUserDTO userToUpdate = new AdminUserDTO("john@doe.fr", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    userToUpdate.setPassword("password");

    adminService.updateUser(clientDTO, tokenDTO, userToUpdate);

    AdminCollaborator updatedUser = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    verify(hrAdminManager).updateCollaborator(updatedUser);
  }
}
