package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private TokenRepository tokenRepository;
  @Mock
  private DateService dateService;

  private TokenService tokenService;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    tokenService = new TokenService(userRepository, tokenRepository, dateService);
  }

  @Test
  public void should_return_ok_if_token_is_valid() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient("client");
    oAuthToken.setUsername("john@doe.fr");
    oAuthToken.setAccessToken("aaa");
    oAuthToken.setRefreshToken("bbb");
    oAuthToken.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));

    given(tokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(oAuthToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));

    ResponseEntity response = tokenService.checkToken(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_unauthorized_if_token_is_not_valid() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    given(tokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(null);

    ResponseEntity response = tokenService.checkToken(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_return_unauthorized_if_token_is_invalid() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient("client");
    oAuthToken.setUsername("john@doe.fr");
    oAuthToken.setAccessToken("aaa");
    oAuthToken.setRefreshToken("bbb");
    oAuthToken.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 10, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));

    given(tokenRepository.findByUsernameClientAndAccessToken("john@doe.fr", "client", "aaa")).willReturn(oAuthToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));

    ResponseEntity response = tokenService.checkToken(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}
