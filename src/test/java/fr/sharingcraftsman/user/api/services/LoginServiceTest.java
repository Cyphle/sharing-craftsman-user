package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import fr.sharingcraftsman.user.infrastructure.models.ApiClient;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
import fr.sharingcraftsman.user.infrastructure.models.User;
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

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private TokenRepository tokenRepository;
  @Mock
  private DateService dateService;

  private LoginService loginService;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    loginService = new LoginService(userRepository, tokenRepository, clientRepository, dateService);
  }

  @Test
  public void should_login_user() throws Exception {
    OAuthToken oAuthToken = new OAuthToken();
    oAuthToken.setClient("client");
    oAuthToken.setUsername("john@doe.fr");
    oAuthToken.setAccessToken("aaa");
    oAuthToken.setRefreshToken("bbb");
    oAuthToken.setExpirationDate(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));

    given(clientRepository.findByNameAndSecret("client", "secret")).willReturn(new ApiClient("client", "clientsecret"));
    given(userRepository.findByUsernameAndPassword("john@doe.fr", "password")).willReturn(new User("john@doe.fr", "password"));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(tokenRepository.save(any(OAuthToken.class))).willReturn(oAuthToken);
    Login login = new Login("client", "secret", "john@doe.fr", "password", true);

    ResponseEntity response = loginService.login(login);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
