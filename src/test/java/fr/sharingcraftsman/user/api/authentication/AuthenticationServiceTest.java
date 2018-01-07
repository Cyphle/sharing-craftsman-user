package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.LoginDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.InvalidToken;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.user.CollaboratorBuilder;
import fr.sharingcraftsman.user.domain.user.User;
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
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private AccessTokenRepository accessTokenRepository;
  @Mock
  private DateService dateService;

  private AuthenticationService authenticationService;
  private ClientDTO clientDTO;
  private AccessToken validToken;
  private TokenDTO token;
  private User user;
  private Credentials credentials;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.knownClient("client", "secret"));

    authenticationService = new AuthenticationService(userRepository, accessTokenRepository, clientRepository, dateService);
    clientDTO = new ClientDTO("client", "secret");
    validToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));

    token = new TokenDTO();
    token.setUsername("john@doe.fr");
    token.setAccessToken("aaa");

    user = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("T49xWf/l7gatvfVwethwDw=="))
            .build();

    credentials = Credentials.buildWithEncryptionAndPersistentLogging("john@doe.fr", "T49xWf/l7gatvfVwethwDw==", true);
  }

  @Test
  public void should_login_user() throws Exception {
    given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(user);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(accessTokenRepository.createNewToken(any(Client.class), any(User.class), any(AccessToken.class))).willReturn(validToken);

    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password", true);
    ResponseEntity response = authenticationService.login(clientDTO, loginDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_return_ok_if_token_is_valid() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));

    ResponseEntity response = authenticationService.checkToken(clientDTO, token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_unauthorized_if_token_is_not_valid() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(new InvalidToken());

    ResponseEntity response = authenticationService.checkToken(clientDTO, token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_return_unauthorized_if_token_has_expired() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2018, Month.JANUARY, 12, 12, 0));

    ResponseEntity response = authenticationService.checkToken(clientDTO, token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_return_ok_when_logout() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));

    ResponseEntity response = authenticationService.logout(clientDTO, token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_generate_new_token_from_refresh_token() throws Exception {
    given(userRepository.findUserFromUsername(credentials.getUsername())).willReturn(user);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(accessTokenRepository.createNewToken(any(Client.class), any(User.class), any(AccessToken.class))).willReturn(validToken);
    given(accessTokenRepository.findTokenFromRefreshToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    TokenDTO refreshToken = new TokenDTO();
    refreshToken.setUsername("john@doe.fr");
    refreshToken.setRefreshToken("bbb");

    ResponseEntity response = authenticationService.refreshToken(clientDTO, refreshToken);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(new TokenDTO("john@doe.fr", "aaa", "bbb", 1514631600000L));
    verify(accessTokenRepository).deleteTokensOf(any(User.class), any(Client.class));
  }
}
