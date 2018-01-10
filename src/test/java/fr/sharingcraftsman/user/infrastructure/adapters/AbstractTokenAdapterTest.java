package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.AbstractToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.AccessTokenJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTokenAdapterTest {
  @Mock
  private AccessTokenJpaRepository accessTokenJpaRepository;
  @Mock
  private DateService dateService;
  private AccessTokenRepository tokenAdapter;
  private AccessToken token;
  private Credentials credentials;
  private Client client;
  private User user;

  @Before
  public void setUp() throws Exception {
    tokenAdapter = new AccessTokenAdapter(accessTokenJpaRepository);
    token = AccessToken.from("aaa", "bbb", LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    credentials = Credentials.buildWithEncryption("john@doe.fr", "password");
    client = Client.from("client", "secret");
    user = User.from(credentials);
  }

  @Test
  public void should_delete_tokens_of_collaborator() throws Exception {
    Mockito.doNothing().when(accessTokenJpaRepository).deleteByUsername(any(String.class), any(String.class));

    tokenAdapter.deleteTokensOf(user, client);

    verify(accessTokenJpaRepository).deleteByUsername("john@doe.fr", "client");
  }

  @Test
  public void should_create_token_for_collaborator() throws Exception {
    AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
    accessTokenEntity.setClient("client");
    accessTokenEntity.setUsername("john@doe.fr");
    accessTokenEntity.setAccessToken(generateToken("clientjohn@doe.fr"));
    accessTokenEntity.setRefreshToken(generateToken("clientjohn@doe.fr"));
    accessTokenEntity.setExpirationDate(DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0)));
    given(accessTokenJpaRepository.save(any(AccessTokenEntity.class))).willReturn(accessTokenEntity);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    AccessToken token = AccessToken.from(
            generateToken(client.getName() + user.getUsername()),
            generateToken(client.getName() + user.getUsername()),
            dateService.getDayAt(8)
    );

    AccessToken createdToken = tokenAdapter.createNewToken(client, user, token);

    assertThat(createdToken.getAccessToken()).hasSize(128);
    assertThat(createdToken.getRefreshToken()).hasSize(128);
    assertThat(createdToken.getExpirationDate()).isEqualTo(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    verify(accessTokenJpaRepository).save(any(AccessTokenEntity.class));
  }

  @Test
  public void should_return_a_valid_token_when_access_token_found() throws Exception {
    given(accessTokenJpaRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(new AccessTokenEntity("john@doe.fr", "client", "aaa", "bbb", DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))));

    AbstractToken foundAbstractToken = tokenAdapter.findTokenFromAccessToken(client, credentials.getUsername(), token);

    assertThat(foundAbstractToken.isValid()).isTrue();
  }

  @Test
  public void should_return_an_invalid_token_when_access_token_not_found() throws Exception {
    given(accessTokenJpaRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(null);

    AbstractToken foundAbstractToken = tokenAdapter.findTokenFromAccessToken(client, credentials.getUsername(), token);

    assertThat(foundAbstractToken.isValid()).isFalse();
  }

  @Test
  public void should_return_a_valid_token_when_refresh_token_found() throws Exception {
    given(accessTokenJpaRepository.findByUsernameClientAndRefreshToken("john@doe.fr", "client", "bbb")).willReturn(new AccessTokenEntity("john@doe.fr", "client", "aaa", "bbb", DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0))));
    AccessToken refreshToken = AccessToken.fromOnlyRefreshToken("bbb");

    AbstractToken foundAbstractToken = tokenAdapter.findTokenFromRefreshToken(client, credentials.getUsername(), refreshToken);

    assertThat(foundAbstractToken.isValid()).isTrue();
  }

  @Test
  public void should_return_an_invalid_token_when_refresh_token_not_found() throws Exception {
    given(accessTokenJpaRepository.findByUsernameClientAndRefreshToken("john@doe.fr", "client", "bbb")).willReturn(null);

    AbstractToken foundAbstractToken = tokenAdapter.findTokenFromRefreshToken(client, credentials.getUsername(), token);

    assertThat(foundAbstractToken.isValid()).isFalse();
  }

  private String generateToken(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
