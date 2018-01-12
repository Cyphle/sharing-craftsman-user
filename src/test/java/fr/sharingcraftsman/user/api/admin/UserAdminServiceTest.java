package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.UnknownUserInfo;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.UnknownClient;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserAdminServiceTest {
  @Mock
  private AdminUserRepository adminUserRepository;
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

    userAdminService = new UserAdminService(clientRepository, userAuthorizationRepository, authorizationRepository, adminUserRepository);
  }

  @Test
  public void should_get_list_of_users_with_profile_and_authorizations() throws Exception {
    given(adminUserRepository.getAllUsers()).willReturn(Arrays.asList(
            User.from("john@doe.fr", "password"),
            User.from("admin@toto.fr", "password")
    ));
    given(adminUserRepository.getAllProfiles()).willReturn(Arrays.asList(
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("www.johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("admin@toto.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"))
    ));
    given(adminUserRepository.getAllTechnicalUserDetails()).willReturn(Arrays.asList(
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));
    given(userAuthorizationRepository.findGroupsOf(Username.from("john@doe.fr"))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));

    ResponseEntity response = userAdminService.getAllUsers(clientDTO, tokenDTO);

    verify(adminUserRepository).getAllUsers();
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
    Mockito.doNothing().when(adminUserRepository).deleteUser(any(Username.class));
    given(adminUserRepository.findUserFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "passwrdo"));

    ResponseEntity response = userAdminService.deleteUser(clientDTO, tokenDTO, "hello@world.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(adminUserRepository).deleteUser(Username.from("hello@world.fr"));
  }

  @Test
  public void should_update_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(adminUserRepository.findUserInfoFromUsername(Username.from("john@doe.fr"))).willReturn(UserInfo.from(
            User.from("john@doe.fr", "password"),
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("www.johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    UserInfoDTO userToUpdate = new UserInfoDTO("john@doe.fr", "John", "Doe", "new@email.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    userToUpdate.setPassword("password");
    userAdminService.updateUser(clientDTO, tokenDTO, userToUpdate);

    UserInfo updatedUser = UserInfo.from(
            User.from("john@doe.fr", "password"),
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("new@email.fr"), Link.to("www.johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    );
    verify(adminUserRepository).updateUser(updatedUser);
  }

  @Test
  public void should_create_a_new_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    given(adminUserRepository.findUserInfoFromUsername(Username.from("john@doe.fr"))).willReturn(new UnknownUserInfo());

    userAdminService.addUser(clientDTO, tokenDTO, user);

    UserInfo newUser = UserInfo.from(
            User.from("john@doe.fr", "password"),
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("www.johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    );
    verify(adminUserRepository).createUser(newUser);
  }
}
