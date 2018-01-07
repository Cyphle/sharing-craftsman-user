package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.InvalidToken;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;
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
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserEntityServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private DateService dateService;
  @Mock
  private AccessTokenRepository accessTokenRepository;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;

  private UserService userService;
  private ClientDTO clientDTO;
  private AccessToken validToken;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 27, 12, 0));
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.knownClient("client", "clietnsercret"));
    userService = new UserService(userRepository, clientRepository, accessTokenRepository, userAuthorizationRepository, authorizationRepository, dateService);
    clientDTO = new ClientDTO("secret", "clientsecret");
    validToken = AccessToken.from("aaa", "bbb", dateService.getDayAt(8));
    tokenDTO = new TokenDTO("john@doe.fr", "aaa");
  }

  @Test
  public void should_register_user() throws Exception {
    given(userRepository.findUserFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(new UnknownUser());
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(userRepository).createNewUser(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_invalid_credential_username_when_username_is_not_specified() throws Exception {
    LoginDTO loginDTO = new LoginDTO("", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Username cannot be empty");
  }

  @Test
  public void should_get_invalid_credential_password_when_username_is_not_specified() throws Exception {
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Password cannot be empty");
  }

  @Test
  public void should_get_user_already_exists_when_using_already_existing_username() throws Exception {
    given(userRepository.findUserFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(
            (new CollaboratorBuilder())
                    .withUsername(usernameBuilder.from("john@doe.fr"))
                    .withPassword(passwordBuilder.from("password"))
                    .build()
    );
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(userRepository, never()).createNewUser(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("User already exists with username: john@doe.fr");
  }

  @Test
  public void should_get_unknown_client_response_when_client_is_not_known() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.unkownClient());
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo("Unknown client");
  }

  @Test
  public void should_get_change_password_token_when_requesting_to_change_password() throws Exception {
    User user = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(user);
    ChangePasswordKey key = new ChangePasswordKey(user, "aaa", LocalDateTime.of(2017, 12, 25, 12, 0));
    given(userRepository.createChangePasswordKeyFor(any(ChangePasswordKey.class))).willReturn(key);
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);

    ResponseEntity response = userService.requestChangePassword(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(new ChangePasswordKeyDTO("aaa"));
  }

  @Test
  public void should_get_unauthorized_if_access_token_is_invalid_when_requesting_password_change() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(new InvalidToken());
    TokenDTO tokenDTO = new TokenDTO("john@doe.fr", "aaa");

    ResponseEntity response = userService.requestChangePassword(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_change_password_when_sending_new_password() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    User user = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("T49xWf/l7gatvfVwethwDw=="))
            .withChangePasswordKey("aaa")
            .withChangePasswordKeyExpirationDate(LocalDateTime.of(2018, Month.JANUARY, 10, 12, 0))
            .build();
    given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(user);

    ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
    changePasswordDTO.setOldPassword("password");
    changePasswordDTO.setNewPassword("newpassword");
    changePasswordDTO.setChangePasswordKey("aaa");
    ResponseEntity response = userService.changePassword(clientDTO, tokenDTO, changePasswordDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_update_profile_with_new_information() throws Exception {
    given(accessTokenRepository.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(AccessToken.class))).willReturn(validToken);
    Profile profile = new ProfileBuilder().withUsername(usernameBuilder.from("john@doe.fr")).withFirstname(Name.of("John")).withLastname(Name.of("Doe")).withEmail(Email.from("john@doe.fr")).withWebsite(Link.to("www.johndoe.fr")).withGithub(Link.to("github.com/johndoe")).withLinkedin(Link.to("linkedin.com/johndoe")).build();
    given(userRepository.findProfileOf(any(Username.class))).willReturn(profile);
    given(userRepository.updateProfileOf(any(Profile.class))).willReturn(profile);

    ProfileDTO profileDTO = new ProfileDTO("John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");

    ResponseEntity response = userService.updateProfile(clientDTO, tokenDTO, profileDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(profileDTO);
  }

  @Test
  public void should_generate_key_when_lost_password() throws Exception {
    User user = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(user);
    ChangePasswordKey key = new ChangePasswordKey(user, "aaa", LocalDateTime.of(2017, 12, 25, 12, 0));
    given(userRepository.createChangePasswordKeyFor(any(ChangePasswordKey.class))).willReturn(key);
    given(userRepository.findProfileOf(any(Username.class))).willReturn(new Profile(usernameBuilder.from("john@doe.fr"), null, null, null, null, null, null));

    ResponseEntity response = userService.generateLostPasswordKey(clientDTO, "john@doe.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}