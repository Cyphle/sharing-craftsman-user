package fr.sharingcraftsman.user.api.common;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.UnknownClient;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationVerifierServiceTest {
  private AuthorizationVerifierService authorizationVerifierService;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    clientDTO = ClientDTO.from("client", "secret");
    tokenDTO = TokenDTO.from("admin", "aaa");

    authorizationVerifierService = new AuthorizationVerifierService(clientRepository, userAuthorizationRepository, authorizationRepository);
  }

  @Test
  public void should_return_null_when_client_and_user_are_authorized() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "secret"));
    given(userAuthorizationRepository.findGroupsOf(any(Username.class))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Collections.singletonList(Role.from("ROLE_ADMIN")));

    assertThat(authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO)).isNull();
  }

  @Test
  public void should_get_unauthorized_client_when_client_is_not_known() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(new UnknownClient());

    assertThat(authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO)).isEqualTo(new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void should_get_unauthorized_user_when_user_is_not_admin() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "secret"));
    given(userAuthorizationRepository.findGroupsOf(any(Username.class))).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));

    assertThat(authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO)).isEqualTo(new ResponseEntity<>("Unauthorized user", HttpStatus.UNAUTHORIZED));
  }
}
