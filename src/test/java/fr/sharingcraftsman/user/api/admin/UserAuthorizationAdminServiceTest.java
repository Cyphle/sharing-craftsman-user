package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

@RunWith(MockitoJUnitRunner.class)
public class UserAuthorizationAdminServiceTest {
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private DateService dateService;

  private UserAuthorizationAdminService userAuthorizationAdminService;

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
    user = UserInfoDTO.from("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);
    user.setPassword("password");

    GroupDTO adminGroup = new GroupDTO("ADMINS");
    adminGroup.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    AuthorizationsDTO adminAuthorization = new AuthorizationsDTO();
    adminAuthorization.addGroup(adminGroup);
    adminUser = UserInfoDTO.from("admin@toto.fr", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", adminAuthorization, true, 1514631600000L, 1514631600000L);
    adminUser.setPassword("password");

    clientDTO = new ClientDTO("client", "secret");
    tokenDTO = TokenDTO.from("admin@toto.fr", "aaa");

    userAuthorizationAdminService = new UserAuthorizationAdminService(clientRepository, userAuthorizationRepository, authorizationRepository);
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    UserGroupDTO newGroupForUser = UserGroupDTO.from("hello@world", "USERS");

    ResponseEntity response = userAuthorizationAdminService.addGroupToUser(clientDTO, tokenDTO, newGroupForUser);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_group_to_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    UserGroupDTO newGroupForUser = UserGroupDTO.from("hello@world", "USERS");

    ResponseEntity response = userAuthorizationAdminService.removeGroupToUser(clientDTO, tokenDTO, newGroupForUser);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
