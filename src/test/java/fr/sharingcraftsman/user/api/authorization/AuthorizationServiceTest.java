package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.api.authorization.AuthorizationService;
import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
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

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private AccessTokenRepository accessTokenRepository;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private DateService dateService;

  private ClientDTO clientDTO;
  private TokenDTO token;
  private AccessToken validToken;
  private AuthorizationService authorizationService;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    authorizationService = new AuthorizationService(userRepository, clientRepository, accessTokenRepository, userAuthorizationRepository, authorizationRepository, dateService);

    clientDTO = new ClientDTO("client", "secret");
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "secret"));

    token = new TokenDTO();
    token.setUsername("john@doe.fr");
    token.setAccessToken("aaa");

    validToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));

    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
  }

  @Test
  public void should_get_authorizations_of_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(usernameBuilder.from("john@doe.fr"))).willReturn(Arrays.asList(Group.from("USERS"), Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));

    ResponseEntity response = authorizationService.getAuthorizations(clientDTO, token);

    GroupDTO groupUser = new GroupDTO("USERS");
    groupUser.addRoles(Collections.singletonList(new RoleDTO("ROLE_USER")));
    GroupDTO groupAdmin = new GroupDTO("ADMINS");
    groupAdmin.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    AuthorizationsDTO expectedAuthorizations = new AuthorizationsDTO();
    expectedAuthorizations.addGroup(groupUser);
    expectedAuthorizations.addGroup(groupAdmin);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(expectedAuthorizations);
  }
}
