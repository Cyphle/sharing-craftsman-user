package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthorizationAdminServiceTest {
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private AuthorizationVerifierService authorizationVerifierService;
  @Mock
  private DateService dateService;

  private UserAuthorizationAdminService userAuthorizationAdminService;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(authorizationVerifierService.isUnauthorizedAdmin(any(ClientDTO.class), any(TokenDTO.class))).willReturn(null);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));

    clientDTO = ClientDTO.from("client", "secret");
    tokenDTO = TokenDTO.from("admin@toto.fr", "aaa");

    userAuthorizationAdminService = new UserAuthorizationAdminService(userAuthorizationRepository, authorizationRepository, authorizationVerifierService);
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    ResponseEntity response = userAuthorizationAdminService.addGroupToUser(
            clientDTO,
            tokenDTO,
            UserGroupDTO.from("hello@world", "USERS")
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_group_to_user() throws Exception {
    ResponseEntity response = userAuthorizationAdminService.removeGroupToUser(
            clientDTO,
            tokenDTO,
            UserGroupDTO.from("hello@world", "USERS")
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
