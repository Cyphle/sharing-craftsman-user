package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.InvalidToken;
import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientStock;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.*;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserEntityServiceTest {
  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;
  @Mock
  private ClientStock clientStock;
  @Mock
  private DateService dateService;
  @Mock
  private TokenAdministrator tokenAdministrator;
  @Mock
  private GroupAdministrator groupAdministrator;
  @Mock
  private RoleAdministrator roleAdministrator;

  private UserService userService;
  private ClientDTO clientDTO;
  private ValidToken validToken;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 27, 12, 0));
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    given(clientStock.findClient(any(Client.class))).willReturn(Client.knownClient("client", "clietnsercret"));
    userService = new UserService(humanResourceAdministrator, clientStock, tokenAdministrator, groupAdministrator, roleAdministrator, dateService);
    clientDTO = new ClientDTO("secret", "clientsecret");
    validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();
    tokenDTO = new TokenDTO("john@doe.fr", "aaa");
  }

  @Test
  public void should_register_user() throws Exception {
    given(humanResourceAdministrator.findCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(new UnknownCollaborator());
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(humanResourceAdministrator).createNewCollaborator(Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false)));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_invalid_credential_username_when_username_is_not_specified() throws Exception {
    LoginDTO loginDTO = new LoginDTO("", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(humanResourceAdministrator, never()).createNewCollaborator(any(Collaborator.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Username cannot be empty");
  }

  @Test
  public void should_get_invalid_credential_password_when_username_is_not_specified() throws Exception {
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(humanResourceAdministrator, never()).createNewCollaborator(any(Collaborator.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Password cannot be empty");
  }

  @Test
  public void should_get_user_already_exists_when_using_already_existing_username() throws Exception {
    given(humanResourceAdministrator.findCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"))).willReturn(
            (new CollaboratorBuilder())
                    .withUsername(usernameBuilder.from("john@doe.fr"))
                    .withPassword(passwordBuilder.from("password"))
                    .build()
    );
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    verify(humanResourceAdministrator, never()).createNewCollaborator(any(Collaborator.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Collaborator already exists with username: john@doe.fr");
  }

  @Test
  public void should_get_unknown_client_response_when_client_is_not_known() throws Exception {
    given(clientStock.findClient(any(Client.class))).willReturn(Client.unkownClient());
    LoginDTO loginDTO = new LoginDTO("john@doe.fr", "password");

    ResponseEntity response = userService.registerUser(clientDTO, loginDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo("Unknown client");
  }

  @Test
  public void should_get_change_password_token_when_requesting_to_change_password() throws Exception {
    Collaborator collaborator = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    given(humanResourceAdministrator.findCollaboratorFromUsername(any(Username.class))).willReturn(collaborator);
    ChangePasswordKey key = new ChangePasswordKey(collaborator, "aaa", LocalDateTime.of(2017, 12, 25, 12, 0));
    given(humanResourceAdministrator.createChangePasswordKeyFor(any(ChangePasswordKey.class))).willReturn(key);
    given(tokenAdministrator.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);

    ResponseEntity response = userService.requestChangePassword(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(new ChangePasswordKeyDTO("aaa"));
  }

  @Test
  public void should_get_unauthorized_if_access_token_is_invalid_when_requesting_password_change() throws Exception {
    given(tokenAdministrator.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(new InvalidToken());
    TokenDTO tokenDTO = new TokenDTO("john@doe.fr", "aaa");

    ResponseEntity response = userService.requestChangePassword(clientDTO, tokenDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_change_password_when_sending_new_password() throws Exception {
    given(tokenAdministrator.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
    Collaborator collaborator = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("T49xWf/l7gatvfVwethwDw=="))
            .withChangePasswordKey("aaa")
            .withChangePasswordKeyExpirationDate(LocalDateTime.of(2018, Month.JANUARY, 10, 12, 0))
            .build();
    given(humanResourceAdministrator.findCollaboratorFromCredentials(any(Credentials.class))).willReturn(collaborator);

    ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
    changePasswordDTO.setOldPassword("password");
    changePasswordDTO.setNewPassword("newpassword");
    changePasswordDTO.setChangePasswordKey("aaa");
    ResponseEntity response = userService.changePassword(clientDTO, tokenDTO, changePasswordDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_update_profile_with_new_information() throws Exception {
    given(tokenAdministrator.findTokenFromAccessToken(any(Client.class), any(Credentials.class), any(ValidToken.class))).willReturn(validToken);
    KnownProfile profile = new ProfileBuilder().withUsername(usernameBuilder.from("john@doe.fr")).withFirstname(Name.of("John")).withLastname(Name.of("Doe")).withEmail(Email.from("john@doe.fr")).withWebsite(Link.to("www.johndoe.fr")).withGithub(Link.to("github.com/johndoe")).withLinkedin(Link.to("linkedin.com/johndoe")).build();
    given(humanResourceAdministrator.findProfileOf(any(Username.class))).willReturn(profile);
    given(humanResourceAdministrator.updateProfileOf(any(KnownProfile.class))).willReturn(profile);

    ProfileDTO profileDTO = new ProfileDTO("John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");

    ResponseEntity response = userService.updateProfile(clientDTO, tokenDTO, profileDTO);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(profileDTO);
  }

  @Test
  public void should_generate_key_when_lost_password() throws Exception {
    Collaborator collaborator = (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("password"))
            .build();
    given(humanResourceAdministrator.findCollaboratorFromUsername(any(Username.class))).willReturn(collaborator);
    ChangePasswordKey key = new ChangePasswordKey(collaborator, "aaa", LocalDateTime.of(2017, 12, 25, 12, 0));
    given(humanResourceAdministrator.createChangePasswordKeyFor(any(ChangePasswordKey.class))).willReturn(key);
    given(humanResourceAdministrator.findProfileOf(any(Username.class))).willReturn(new KnownProfile(usernameBuilder.from("john@doe.fr"), null, null, null, null, null, null));

    ResponseEntity response = userService.generateLostPasswordKey(clientDTO, "john@doe.fr");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  /*
  @Test
  public void should_send_exception_when_no_email_is_set() throws Exception {

  }
   */
}
