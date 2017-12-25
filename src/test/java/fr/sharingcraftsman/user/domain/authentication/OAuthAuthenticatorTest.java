package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.utils.DateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static fr.sharingcraftsman.user.domain.company.Collaborator.collaboratorBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

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
    identifier = new OAuthAuthenticator(humanResourceAdministrator, tokenAdministrator);
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

    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, 12, 25, 12, 0));
    given(humanResourceAdministrator.findFromCredentials(any(Credentials.class))).willReturn(collaborator);
    given(tokenAdministrator.createNewToken(collaborator, client, true)).willReturn(token);

    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));
    credentials.setStayLogged(true);

    Token expectedToken = identifier.login(credentials, client);

    assertThat(expectedToken).isEqualTo(expectedToken);
  }
}
