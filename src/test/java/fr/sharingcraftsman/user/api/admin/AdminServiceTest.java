package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.admin.exceptions.UnknownBaseUserForAdminCollaborator;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.CollaboratorBuilder;
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
import java.util.*;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
  @Mock
  private UserForAdminRepository userForAdminRepository;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private DateService dateService;

  private AdminService adminService;

  private ClientDTO clientDTO;
  private AccessToken validToken;
  private TokenDTO tokenDTO;
  private AdminUserDTO user;
  private AdminUserDTO adminUser;

  @Before
  public void setUp() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.knownClient("client", "secret"));

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
    validToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));

    tokenDTO = new TokenDTO();
    tokenDTO.setUsername("admin@toto.fr");
    tokenDTO.setAccessToken("aaa");

    adminService = new AdminService(userForAdminRepository, clientRepository, userAuthorizationRepository, authorizationRepository);
  }

  @Test
  public void should_get_list_of_users_with_profile_and_authorizations() throws Exception {
    List<UserForAdmin> collaborators = Arrays.asList(
            UserForAdmin.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "", null, true, new Date(), new Date()),
            UserForAdmin.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date())
    );
    given(userForAdminRepository.getAllCollaborators()).willReturn(collaborators);
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("john@doe.fr"))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    verify(userForAdminRepository).getAllCollaborators();
    assertThat(response.getBody()).isEqualTo(Arrays.asList(user, adminUser));
  }

  @Test
  public void should_get_unauthorized_if_client_is_not_known() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.unkownClient());

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_get_unauthorized_if_requester_has_not_role_admin() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));

    ResponseEntity response = adminService.getUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_delete_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Mockito.doNothing().when(userForAdminRepository).deleteCollaborator(any(Username.class));
    given(userForAdminRepository.findCollaboratorFromUsername(usernameBuilder.from("hello@world.fr"))).willReturn(
            new CollaboratorBuilder()
                    .withUsername(usernameBuilder.from("hello@world.fr"))
                    .withPassword(passwordBuilder.from("passwrdo"))
                    .build()
    );

    ResponseEntity response = adminService.deleteUser(clientDTO, tokenDTO, "hello@world.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(userForAdminRepository).deleteCollaborator(usernameBuilder.from("hello@world.fr"));
  }

  @Test
  public void should_update_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(userForAdminRepository.findAdminCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(
            UserForAdmin.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "", null, true, new Date(), new Date())
    );

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    AdminUserDTO userToUpdate = new AdminUserDTO("john@doe.fr", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    userToUpdate.setPassword("password");
    adminService.updateUser(clientDTO, tokenDTO, userToUpdate);

    UserForAdmin updatedUser = UserForAdmin.from("john@doe.fr", "password", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "", null, true, new Date(), new Date());
    verify(userForAdminRepository).updateCollaborator(updatedUser);
  }

  @Test
  public void should_create_a_new_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(userForAdminRepository.findAdminCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(new UnknownBaseUserForAdminCollaborator());

    adminService.addUser(clientDTO, tokenDTO, user);

    UserForAdmin newCollaborator = UserForAdmin.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "", null, true, new Date(), new Date());
    verify(userForAdminRepository).createCollaborator(newCollaborator);
  }

  @Test
  public void should_get_groups_and_roles() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Group users = Group.from("USERS");
    users.addRole(Role.from("ROLE_USER"));
    Group admins = Group.from("ADMINS");
    admins.addRoles(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<Group> groups = new HashSet<>();
    groups.add(users);
    groups.add(admins);
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(groups);

    ResponseEntity response = adminService.getGroups(clientDTO, tokenDTO);

    GroupDTO groupUser = new GroupDTO("USERS");
    groupUser.addRoles(Collections.singletonList(new RoleDTO("ROLE_USER")));
    GroupDTO groupAdmin = new GroupDTO("ADMINS");
    groupAdmin.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    Set<GroupDTO> groupsDTO = new HashSet<>();
    groupsDTO.add(groupUser);
    groupsDTO.add(groupAdmin);
    assertThat(response.getBody()).isEqualTo(groupsDTO);
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    UserGroupDTO newGroupForUser = new UserGroupDTO("hello@world", "USERS");

    ResponseEntity response = adminService.addGroupToUser(clientDTO, tokenDTO, newGroupForUser);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_group_to_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    UserGroupDTO newGroupForUser = new UserGroupDTO("hello@world", "USERS");

    ResponseEntity response = adminService.removeGroupToUser(clientDTO, tokenDTO, newGroupForUser);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_create_new_group_with_roles() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(new RoleDTO("ROLE_ROOT"));
    roles.add(new RoleDTO("ROLE_ADMIN"));
    roles.add(new RoleDTO("ROLE_USER"));
    GroupDTO newGroup = new GroupDTO("SUPER_ADMINS", roles);

    ResponseEntity response = adminService.createNewGroupWithRoles(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(new RoleDTO("ROLE_ROOT"));
    roles.add(new RoleDTO("ROLE_ADMIN"));
    roles.add(new RoleDTO("ROLE_USER"));
    GroupDTO newGroup = new GroupDTO("SUPER_ADMINS", roles);

    ResponseEntity response = adminService.removeRoleFromGroup(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
