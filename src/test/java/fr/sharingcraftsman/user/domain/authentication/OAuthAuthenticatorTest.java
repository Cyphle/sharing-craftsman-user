package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static fr.sharingcraftsman.user.domain.company.Collaborator.collaboratorBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OAuthAuthenticatorTest {
  private Authenticator identifier;

  @Mock
  private DateService dateService;

  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;

  @Mock
  private TokenAdministrator tokenAdministrator;

  @Before
  public void setUp() throws Exception {
    identifier = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator, dateService);
  }

  @Test
  public void should_generate_token_when_identifying() throws Exception {
    Collaborator collaborator = collaboratorBuilder
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    Client client = Client.knownClient("client", "secret");
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();

    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(humanResourceAdministrator.findFromCredentials(any(Credentials.class))).willReturn(collaborator);
    given(tokenAdministrator.createNewToken(client, collaborator, token)).willReturn(token);

    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    credentials.setStayLogged(true);

    Token expectedToken = identifier.login(credentials, client);

    assertThat(expectedToken).isEqualTo(expectedToken);
  }

  @Test
  public void should_validate_token() throws Exception {
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))
            .build();
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Client client = Client.knownClient("client", "secret");
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(tokenAdministrator.findTokenFor(client, credentials, token)).willReturn(token);

    boolean isValid = identifier.isTokenValid(credentials, client, token);

    assertThat(isValid).isTrue();
  }

  @Test
  public void should_not_validate_token() throws Exception {
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))
            .build();
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Client client = Client.knownClient("client", "secret");
    given(tokenAdministrator.findTokenFor(client, credentials, token)).willReturn(new InvalidToken());

    boolean isValid = identifier.isTokenValid(credentials, client, token);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_not_validate_token_if_is_expired() throws Exception {
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("")
            .expiringThe(null)
            .build();
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    ValidToken fetchedToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 10, 12, 0))
            .build();
    Client client = Client.knownClient("client", "secret");
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(tokenAdministrator.findTokenFor(client, credentials, token)).willReturn(fetchedToken);

    boolean isValid = identifier.isTokenValid(credentials, client, token);

    assertThat(isValid).isFalse();
  }

  @Test
  public void should_delete_token_when_logout() throws Exception {
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))
            .build();
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Client client = Client.knownClient("client", "secret");
    given(tokenAdministrator.findTokenFor(client, credentials, token)).willReturn(token);
    Collaborator collaborator = collaboratorBuilder
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    given(humanResourceAdministrator.findFromCredentials(credentials)).willReturn(collaborator);

    identifier.logout(credentials, client, token);

    verify(tokenAdministrator).deleteTokensOf(collaborator, client);
  }
}
