package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.*;
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

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Base64;
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
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Person collaborator = Collaborator.from(credentials);
    Client client = Client.knownClient("client", "secret");

    tokenAdapter.deleteTokensOf((Collaborator) collaborator, client);

    verify(tokenRepository).deleteByUsername("john@doe.fr", "client");
  }

  @Test
  public void should_create_token_for_collaborator() throws Exception {
    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient("client");
    oAuthToken.setUsername("john@doe.fr");
    oAuthToken.setAccessToken(generateKey("clientjohn@doe.fr"));
    oAuthToken.setRefreshToken(generateKey("clientjohn@doe.fr"));
    oAuthToken.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(tokenRepository.save(any(OAuthToken.class))).willReturn(oAuthToken);
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Person collaborator = Collaborator.from(credentials);
    Client client = Client.knownClient("client", "secret");

    ValidToken token = tokenAdapter.createNewToken((Collaborator) collaborator, client, false);

    assertThat(token.getAccessToken()).hasSize(128);
    assertThat(token.getRefreshToken()).hasSize(128);
    assertThat(token.getExpirationDate()).isEqualTo(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    verify(tokenRepository).save(any(OAuthToken.class));
  }

  @Test
  public void should_return_a_valid_token_when_found() throws Exception {
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))
            .build();
    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    Client client = Client.knownClient("client", "secret");
    given(tokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(new OAuthToken("john@doe.fr", "client", "aaa", "bbb", Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant())));

    Token foundToken = tokenAdapter.findTokenFor(client, credentials, token);

    assertThat(foundToken.isValid()).isTrue();
  }

  private String generateKey(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
