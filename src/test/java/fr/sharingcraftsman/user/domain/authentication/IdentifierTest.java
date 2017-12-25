package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.utils.DateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class IdentifierTest {
  private Authenticator identifier;

  @Mock
  private DateService dateService;

  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;

  @Mock
  private TokenAdministrator tokenAdministrator;

  @Before
  public void setUp() throws Exception {
    identifier = new Identifier(dateService, humanResourceAdministrator, tokenAdministrator);
  }

  @Test
  public void should_generate_token_when_identifying() throws Exception {
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));
    credentials.stayLogged(true);

    Token token = identifier.identify(credentials);

    assertThat(token).isEqualTo(validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(7))
            .build());
  }
}
