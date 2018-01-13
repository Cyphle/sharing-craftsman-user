package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.authentication.LoginDTO;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.InvalidToken;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.ports.ChangePasswordTokenRepository;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private AuthorizationVerifierService authorizationVerifierService;
  @Mock
  private DateService dateService;
  @Mock
  private AccessTokenRepository accessTokenRepository;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private ChangePasswordTokenRepository changePasswordTokenRepository;

  private UserService userService;
  private ClientDTO clientDTO;
  private AccessToken validToken;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0)));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 27, 12, 0));
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    given(authorizationVerifierService.isUnauthorizedClient(any(ClientDTO.class))).willReturn(false);

    userService = new UserService(userRepository, accessTokenRepository, userAuthorizationRepository, authorizationRepository, changePasswordTokenRepository, dateService, authorizationVerifierService);
    clientDTO = ClientDTO.from("secret", "clientsecret");
    validToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));
    tokenDTO = TokenDTO.from("john@doe.fr", "aaa");
  }

  @Test
  public void should_register_user() throws Exception {
    given(userRepository.findUserFromUsername(Username.from("john@doe.fr"))).willReturn(new UnknownUser());

    ResponseEntity response = userService.registerUser(clientDTO, LoginDTO.from("john@doe.fr", "password"));

    verify(userRepository).createNewUser(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_invalid_credential_username_when_username_is_not_specified() throws Exception {
    ResponseEntity response = userService.registerUser(clientDTO, LoginDTO.from("", "password"));

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Username cannot be empty");
  }

  @Test
  public void should_get_invalid_credential_password_when_username_is_not_specified() throws Exception {

    ResponseEntity response = userService.registerUser(clientDTO, LoginDTO.from("john@doe.fr", ""));

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Password cannot be empty");
  }

  @Test
  public void should_get_user_already_exists_when_using_already_existing_username() throws Exception {
    given(userRepository.findUserFromUsername(Username.from("john@doe.fr"))).willReturn(User.from("john@doe.fr", "password"));

    ResponseEntity response = userService.registerUser(clientDTO, LoginDTO.from("john@doe.fr", "password"));

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("User already exists with username: john@doe.fr");
  }

  @Test
  public void should_get_unknown_client_response_when_client_is_not_known() throws Exception {
    given(authorizationVerifierService.isUnauthorizedClient(any(ClientDTO.class))).willReturn(true);

    ResponseEntity response = userService.registerUser(clientDTO, LoginDTO.from("john@doe.fr", "password"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo("Unknown client");
  }

  @Test
  public void should_get_change_password_token_when_requesting_to_change_password() throws Exception {
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(User.from("john@doe.fr", "password"));
    given(changePasswordTokenRepository.createChangePasswordTokenFor(any(ChangePasswordToken.class))).willReturn(ChangePasswordToken.from(User.from("john@doe.fr", "password"), "aaa", LocalDateTime.of(2017, 12, 25, 12, 0)));
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Username.class), any(AccessToken.class))).willReturn(validToken);

    ResponseEntity response = userService.getChangePasswordToken(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(ChangePasswordTokenDTO.from("aaa"));
  }

  @Test
  public void should_get_unauthorized_if_access_token_is_invalid_when_requesting_password_change() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Username.class), any(AccessToken.class))).willReturn(new InvalidToken());

    ResponseEntity response = userService.getChangePasswordToken(clientDTO, TokenDTO.from("john@doe.fr", "aaa"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_change_password_when_sending_new_password() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Username.class), any(AccessToken.class))).willReturn(validToken);
    given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(User.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));
    given(changePasswordTokenRepository.findByUsername(any(Username.class))).willReturn(ChangePasswordToken.from(User.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="), "aaa", LocalDateTime.of(2018, Month.MARCH, 10, 0, 0)));

    ResponseEntity response = userService.changePassword(
            clientDTO,
            tokenDTO,
            ChangePasswordDTO.from("aaa", "password", "newpassword")
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_update_profile_with_new_information() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Username.class), any(AccessToken.class))).willReturn(validToken);
    Profile profile = Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("www.johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe"));
    given(userRepository.findProfileOf(any(Username.class))).willReturn(profile);
    given(userRepository.updateProfileOf(any(Profile.class))).willReturn(profile);

    ResponseEntity response = userService.updateProfile(
            clientDTO,
            tokenDTO,
            ProfileDTO.from("John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe")
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(ProfileDTO.from("John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe"));
  }

  @Test
  public void should_generate_token_when_lost_password() throws Exception {
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(User.from("john@doe.fr", "password"));
    given(changePasswordTokenRepository.createChangePasswordTokenFor(any(ChangePasswordToken.class))).willReturn(ChangePasswordToken.from(User.from("john@doe.fr", "password"), "aaa", LocalDateTime.of(2017, 12, 25, 12, 0)));
    given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("john@doe.fr"), null, null, null, null, null, null));

    ResponseEntity response = userService.getLostPasswordToken(clientDTO, "john@doe.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
