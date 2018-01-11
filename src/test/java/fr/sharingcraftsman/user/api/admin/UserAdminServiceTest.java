package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.UnknownUserInfo;
import fr.sharingcraftsman.user.domain.admin.UserInfoOld;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.UnknownClient;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.User;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserAdminServiceTest {
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
  
  private UserAdminService userAdminService;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;
  private UserInfoDTO user;
  private UserInfoDTO adminUser;

  @Before
  public void setUp() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "secret"));

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    user = new UserInfoDTO("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    user.setPassword("password");

    GroupDTO adminGroup = new GroupDTO("ADMINS");
    adminGroup.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    AuthorizationsDTO adminAuthorization = new AuthorizationsDTO();
    adminAuthorization.addGroup(adminGroup);
    adminUser = new UserInfoDTO("admin@toto.fr", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", adminAuthorization, true, 1514631600000L, 1514631600000L);
    adminUser.setPassword("password");

    clientDTO = new ClientDTO("client", "secret");

    tokenDTO = new TokenDTO();
    tokenDTO.setUsername("admin@toto.fr");
    tokenDTO.setAccessToken("aaa");

    userAdminService = new UserAdminService(userForAdminRepository, clientRepository, userAuthorizationRepository, authorizationRepository);
  }

  @Test
  public void should_get_list_of_users_with_profile_and_authorizations() throws Exception {
    List<UserInfoOld> users = Arrays.asList(
            UserInfoOld.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", true, new Date(), new Date()),
            UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date())
    );
    given(userForAdminRepository.getAllUsers()).willReturn(users);
    given(userAuthorizationRepository.findGroupsOf(Username.from("john@doe.fr"))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));

    ResponseEntity response = userAdminService.getAllUsers(clientDTO, tokenDTO);

    verify(userForAdminRepository).getAllUsers();
    assertThat(response.getBody()).isEqualTo(Arrays.asList(user, adminUser));
  }

  @Test
  public void should_get_unauthorized_if_client_is_not_known() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(UnknownClient.get());

    ResponseEntity response = userAdminService.getAllUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_get_unauthorized_if_requester_has_not_role_admin() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));

    ResponseEntity response = userAdminService.getAllUsers(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_delete_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Mockito.doNothing().when(userForAdminRepository).deleteUser(any(Username.class));
    given(userForAdminRepository.findUserFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "passwrdo"));

    ResponseEntity response = userAdminService.deleteUser(clientDTO, tokenDTO, "hello@world.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(userForAdminRepository).deleteUser(Username.from("hello@world.fr"));
  }

  @Test
  public void should_update_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(userForAdminRepository.findAdminUserFromUsername(Username.from("john@doe.fr"))).willReturn(
            UserInfoOld.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", true, new Date(), new Date())
    );

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    UserInfoDTO userToUpdate = new UserInfoDTO("john@doe.fr", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    userToUpdate.setPassword("password");
    userAdminService.updateUser(clientDTO, tokenDTO, userToUpdate);

    UserInfoOld updatedUser = UserInfoOld.from("john@doe.fr", "password", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", true, new Date(), new Date());
    verify(userForAdminRepository).updateUser(updatedUser);
  }

  @Test
  public void should_create_a_new_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(userForAdminRepository.findAdminUserFromUsername(Username.from("john@doe.fr"))).willReturn(new UnknownUserInfo());

    userAdminService.addUser(clientDTO, tokenDTO, user);

    UserInfoOld newUser = UserInfoOld.from("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", true, new Date(), new Date());
    verify(userForAdminRepository).createUser(newUser);
  }
}
