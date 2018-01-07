package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.AccessTokenRepository;
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
  private AccessTokenRepository accessTokenRepository;
  @Mock
  private DateService dateService;
  private TokenAdministrator tokenAdapter;
  private ValidToken token;
  private Credentials credentials;
  private Client client;
  private User user;

  @Before
  public void setUp() throws Exception {
    tokenAdapter = new TokenAdapter(accessTokenRepository);
    token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))
            .build();
    credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);
    client = Client.knownClient("client", "secret");
    user = User.from(credentials);
  }

  @Test
  public void should_delete_tokens_of_collaborator() throws Exception {
    Mockito.doNothing().when(accessTokenRepository).deleteByUsername(any(String.class), any(String.class));

    tokenAdapter.deleteTokensOf(user, client);

    verify(accessTokenRepository).deleteByUsername("john@doe.fr", "client");
  }

  @Test
  public void should_create_token_for_collaborator() throws Exception {
    AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
    accessTokenEntity.setClient("client");
    accessTokenEntity.setUsername("john@doe.fr");
    accessTokenEntity.setAccessToken(generateKey("clientjohn@doe.fr"));
    accessTokenEntity.setRefreshToken(generateKey("clientjohn@doe.fr"));
    accessTokenEntity.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(accessTokenRepository.save(any(AccessTokenEntity.class))).willReturn(accessTokenEntity);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    ValidToken token = validTokenBuilder
            .withAccessToken(generateKey(client.getName() + user.getUsername()))
            .withRefreshToken(generateKey(client.getName() + user.getUsername()))
            .expiringThe(dateService.getDayAt(8))
            .build();

    ValidToken createdToken = tokenAdapter.createNewToken(client, user, token);

    assertThat(createdToken.getAccessToken()).hasSize(128);
    assertThat(createdToken.getRefreshToken()).hasSize(128);
    assertThat(createdToken.getExpirationDate()).isEqualTo(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    verify(accessTokenRepository).save(any(AccessTokenEntity.class));
  }

  @Test
  public void should_return_a_valid_token_when_access_token_found() throws Exception {
    given(accessTokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(new AccessTokenEntity("john@doe.fr", "client", "aaa", "bbb", Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant())));

    Token foundToken = tokenAdapter.findTokenFromAccessToken(client, credentials, token);

    assertThat(foundToken.isValid()).isTrue();
  }

  @Test
  public void should_return_an_invalid_token_when_access_token_not_found() throws Exception {
    given(accessTokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(null);

    Token foundToken = tokenAdapter.findTokenFromAccessToken(client, credentials, token);

    assertThat(foundToken.isValid()).isFalse();
  }

  @Test
  public void should_return_a_valid_token_when_refresh_token_found() throws Exception {
    given(accessTokenRepository.findByUsernameClientAndRefreshToken("john@doe.fr", "client", "bbb")).willReturn(new AccessTokenEntity("john@doe.fr", "client", "aaa", "bbb", Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant())));
    ValidToken refreshToken = validTokenBuilder
            .withAccessToken("")
            .withRefreshToken("bbb")
            .expiringThe(null)
            .build();

    Token foundToken = tokenAdapter.findTokenFromRefreshToken(client, credentials, refreshToken);

    assertThat(foundToken.isValid()).isTrue();
  }

  @Test
  public void should_return_an_invalid_token_when_refresh_token_not_found() throws Exception {
    given(accessTokenRepository.findByUsernameClientAndRefreshToken("john@doe.fr", "client", "bbb")).willReturn(null);

    Token foundToken = tokenAdapter.findTokenFromRefreshToken(client, credentials, token);

    assertThat(foundToken.isValid()).isFalse();
  }

  private String generateKey(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
