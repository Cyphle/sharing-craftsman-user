package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.Person;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TokenAdapterTest {
  @Mock
  private TokenRepository tokenRepository;
  @Mock
  private DateService dateService;
  private TokenAdministrator tokenAdapter;

  @Before
  public void setUp() throws Exception {
    tokenAdapter = new TokenAdapter(tokenRepository, dateService);
  }

  @Test
  public void should_delete_tokens_of_collaborator() throws Exception {
    Mockito.doNothing().when(tokenRepository).deleteByUsername(any(String.class), any(String.class));
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));
    Person collaborator = Collaborator.from(credentials);
    Client client = Client.knownClient("client", "secret");

    tokenAdapter.deleteTokensOf((Collaborator) collaborator, client);

    verify(tokenRepository).deleteByUsername("john@doe.fr", "client");
  }

  @Test
  public void should_create_token_for_collaborator() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, 12, 25, 12, 0));
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));
    Person collaborator = Collaborator.from(credentials);
    Client client = Client.knownClient("client", "secret");

    ValidToken token = tokenAdapter.createNewToken((Collaborator) collaborator, client, false);

    assertThat(token.getAccessToken()).hasSize(128);
    assertThat(token.getRefreshToken()).hasSize(128);
    assertThat(token.getExpirationDate()).isEqualTo(LocalDateTime.of(2017, 12, 25, 12, 0));
    verify(tokenRepository).save(any(OAuthToken.class));
  }
}
