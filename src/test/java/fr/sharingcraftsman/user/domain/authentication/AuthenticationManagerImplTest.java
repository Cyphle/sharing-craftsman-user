package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationManagerImplTest {
  private AuthenticationManager identifier;

  @Mock
  private DateService dateService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccessTokenRepository accessTokenRepository;
  private AccessToken oAuthToken;
  private Client client;
  private Credentials credentials;
  private User user;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    identifier = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    oAuthToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));
    client = Client.from("client", "secret");
    credentials = Credentials.buildWithEncryption("john@doe.fr", "password");
    user = User.from("john@doe.fr", "password");
  }

  @Test
  public void should_generate_token_when_identifying() throws Exception {
    AccessToken token = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));
    given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(user);
    given(accessTokenRepository.createNewToken(any(Client.class), any(User.class), any(AccessToken.class))).willReturn(token);
    credentials.setPersistentLogging(true);

    BaseToken expectedBaseToken = identifier.login(client, credentials);

    assertThat(expectedBaseToken).isEqualTo(token);
  }

  @Test
  public void should_validate_token() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(client, credentials.getUsername(), oAuthToken)).willReturn(oAuthToken);

    boolean isValid = identifier.isTokenValid(client, credentials.getUsername(), oAuthToken);

    assertThat(isValid).isTrue();
  }

  @Test
  public void should_not_validate_access_token_if_does_not_exists() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(client, credentials.getUsername(), oAuthToken)).willReturn(new InvalidToken());

    boolean isValid = identifier.isTokenValid(client, credentials.getUsername(), oAuthToken);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_not_validate_token_if_is_expired() throws Exception {
    AccessToken token = AccessToken.fromOnlyAccessToken("aaa");
    AccessToken fetchedToken = AccessToken.from("aaa", "bbb", LocalDateTime.of(2017, Month.DECEMBER, 10, 12, 0));
    given(accessTokenRepository.findTokenFromAccessToken(client, credentials.getUsername(), token)).willReturn(fetchedToken);

    boolean isValid = identifier.isTokenValid(client, credentials.getUsername(), token);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_delete_token_when_logout() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(client, credentials.getUsername(), oAuthToken)).willReturn(oAuthToken);
    given(userRepository.findUserFromUsername(credentials.getUsername())).willReturn(user);

    identifier.logout(client, credentials.getUsername(), oAuthToken);

    verify(accessTokenRepository).deleteTokensOf(user, client);
  }

  @Test
  public void should_validate_refresh_token() throws Exception {
    given(accessTokenRepository.findTokenFromRefreshToken(client, credentials.getUsername(), oAuthToken)).willReturn(oAuthToken);

    boolean isValid = identifier.isRefreshTokenValid(client, credentials.getUsername(), oAuthToken);

    assertThat(isValid).isTrue();
  }

  @Test
  public void should_not_validate_refresh_token_if_does_not_exists() throws Exception {
    given(accessTokenRepository.findTokenFromRefreshToken(client, credentials.getUsername(), oAuthToken)).willReturn(new InvalidToken());

    boolean isValid = identifier.isRefreshTokenValid(client, credentials.getUsername(), oAuthToken);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_not_validate_refresh_token_if_is_expired() throws Exception {
    AccessToken token = AccessToken.fromOnlyRefreshToken("bbb");
    AccessToken fetchedToken = AccessToken.from("aaa", "bbb", LocalDateTime.of(2017, Month.DECEMBER, 10, 12, 0));
    given(accessTokenRepository.findTokenFromRefreshToken(client, credentials.getUsername(), token)).willReturn(fetchedToken);

    boolean isValid = identifier.isRefreshTokenValid(client, credentials.getUsername(), token);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_generate_new_token_when_request_with_refresh_token() throws Exception {
    AccessToken token = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(user);
    given(accessTokenRepository.createNewToken(any(Client.class), any(User.class), any(AccessToken.class))).willReturn(token);
    credentials.setPersistentLogging(true);

    BaseToken expectedBaseToken = identifier.createNewToken(client, credentials.getUsername());

    assertThat(expectedBaseToken).isEqualTo(token);
  }
}
