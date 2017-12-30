package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.authorization.Authorizations;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
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

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {
  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;
  @Mock
  private ClientStock clientStock;
  @Mock
  private TokenAdministrator tokenAdministrator;
  @Mock
  private GroupAdministrator groupAdministrator;
  @Mock
  private RoleAdministrator roleAdministrator;
  @Mock
  private DateService dateService;

  private ClientDTO clientDTO;
  private TokenDTO token;
  private ValidToken validToken;
  private RoleService roleService;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 25, 12, 0));
    roleService = new RoleService(humanResourceAdministrator, clientStock, tokenAdministrator, groupAdministrator, roleAdministrator, dateService);

    clientDTO = new ClientDTO("client", "secret");
    given(clientStock.findClient(any(Client.class))).willReturn(Client.knownClient("client", "secret"));

    token = new TokenDTO();
    token.setUsername("john@doe.fr");
    token.setAccessToken("aaa");

    validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();
    given(tokenAdministrator.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
  }

  @Test
  public void should_get_authorizations_of_user() throws Exception {
    ResponseEntity response = roleService.getAuthorizations(clientDTO, token);

    Authorizations expectedAuthorizations = new Authorizations();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(expectedAuthorizations);
  }
}
