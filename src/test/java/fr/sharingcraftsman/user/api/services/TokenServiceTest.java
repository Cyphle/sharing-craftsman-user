package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.InvalidToken;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
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
import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static fr.sharingcraftsman.user.domain.company.Collaborator.collaboratorBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;
  @Mock
  private ClientStock clientStock;
  @Mock
  private TokenAdministrator tokenAdministrator;
  @Mock
  private DateService dateService;

  private TokenService tokenService;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));
    tokenService = new TokenService(humanResourceAdministrator, tokenAdministrator, clientStock, dateService);
  }

  @Test
  public void should_login_user() throws Exception {
    Client client = Client.from("client", "secret");
    given(clientStock.findClient(client)).willReturn(Client.knownClient("client", "secret"));
    Credentials credentials = Credentials.buildCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("T49xWf/l7gatvfVwethwDw=="), false);
    Collaborator collaborator = collaboratorBuilder
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("T49xWf/l7gatvfVwethwDw=="))
            .build();
    given(humanResourceAdministrator.findFromCredentials(credentials)).willReturn(collaborator);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    ValidToken token = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(1))
            .build();
    given(tokenAdministrator.createNewToken(any(Client.class), any(Collaborator.class), any(ValidToken.class))).willReturn(token);

    Login login = new Login("client", "secret", "john@doe.fr", "password", true);
    ResponseEntity response = tokenService.login(login);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_return_ok_if_token_is_valid() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    ValidToken validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();
    given(tokenAdministrator.findTokenFor(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
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
    given(tokenAdministrator.findTokenFor(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(new InvalidToken());

    ResponseEntity response = tokenService.checkToken(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_return_unauthorized_if_token_has_expired() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    ValidToken validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();
    given(tokenAdministrator.findTokenFor(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2018, Month.JANUARY, 12, 12, 0));

    ResponseEntity response = tokenService.checkToken(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_return_ok_when_logout() throws Exception {
    fr.sharingcraftsman.user.api.models.OAuthToken token = new fr.sharingcraftsman.user.api.models.OAuthToken();
    token.setUsername("john@doe.fr");
    token.setClient("client");
    token.setAccessToken("aaa");
    token.setRefreshToken("bbb");
    token.setExpirationDate(0);

    ValidToken validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();
    given(tokenAdministrator.findTokenFor(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));

    ResponseEntity response = tokenService.logout(token);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
