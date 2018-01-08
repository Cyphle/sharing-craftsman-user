package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserOrganisationImplTest {
  @Mock
  UserRepository userRepository;
  @Mock
  DateService dateService;

  private Credentials credentials;
  private UserOrganisationImpl userOrganisationImpl;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    credentials = Credentials.buildWithEncryption("john@doe.fr", "password");
    userOrganisationImpl = new UserOrganisationImpl(userRepository, dateService);
  }

  @Test
  public void should_save_user_when_registering() throws Exception {
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(new UnknownUser());
    Credentials credentials = Credentials.build("john@doe.fr", "password");

    userOrganisationImpl.createNewCollaborator(credentials);

    User updatedUser = User.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    verify(userRepository).createNewUser(updatedUser);
  }

  @Test
  public void should_throw_collaborator_exception_if_user_already_exists() throws Exception {
    try {
      given(userRepository.findUserFromUsername(any(Username.class))).willReturn(User.from(Username.from("john@doe.fr")));
      Credentials credentials = Credentials.buildWithEncryption("john@doe.fr", "password");

      userOrganisationImpl.createNewCollaborator(credentials);
      fail("Should throw UserException");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("User already exists with username: john@doe.fr");
    }
  }

  @Test
  public void should_delete_change_request_key_and_create_change_request_key() throws Exception {
    User user = User.from("john@doe.fr",  "password");
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(user);
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    Username username = Username.from("john@doe.fr");

    userOrganisationImpl.createChangePasswordTokenFor(username);

    verify(userRepository).deleteChangePasswordKeyOf(username);
    verify(userRepository).createChangePasswordKeyFor(any(ChangePasswordKey.class));
  }

  @Test
  public void should_throw_exception_if_collaborator_does_not_exists_when_creating_change_password_key() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    given(userRepository.findUserFromUsername(Username.from("john@doe.fr"))).willReturn(new UnknownUser());

    try {
      Username username = Username.from("john@doe.fr");
      userOrganisationImpl.createChangePasswordTokenFor(username);
      fail("Should have throw unknown collaborator exception");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown collaborator");
    }
  }

  @Test
  public void should_change_password_with_new_password() throws Exception {
    User user = User.from(
            Username.from("john@doe.fr"),
            Password.from("T49xWf/l7gatvfVwethwDw=="),
            "aaa",
            LocalDateTime.of(2018, Month.JANUARY, 10, 12, 0));
    given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(user);
    ChangePassword changePassword = ChangePassword.from("aaa", "password", "newpassword");

    userOrganisationImpl.changePassword(credentials, changePassword);

    User updatedUser = User.from(
            Username.from("john@doe.fr"),
            Password.from("hXYHz1OSnuod1SuvLcgD4A=="),
            "aaa",
            LocalDateTime.of(2018, Month.JANUARY, 10, 12, 0));
    verify(userRepository).updateUserPassword(updatedUser);
  }

  @Test
  public void should_throw_unknown_collaborator_exception_if_collaborator_is_not_known() throws Exception {
    try {
      given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(new UnknownUser());
      ChangePassword changePassword = ChangePassword.from("aaa", "password", "newpassword");

      userOrganisationImpl.changePassword(credentials, changePassword);
      fail("Should throw unkown collaborator exception");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown collaborator");
    }
  }

  @Test
  public void should_throw_invalid_change_password_key_exception_if_key_is_not_valid() throws Exception {
    try {
      given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(User.from(credentials));
      ChangePassword changePassword = ChangePassword.from("aaa", "password", "newpassword");

      userOrganisationImpl.changePassword(credentials, changePassword);
      fail("Should throw invalid change password key exception");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Invalid token to change password");
    }
  }

  @Test
  public void should_throw_invalid_change_password_key_exception_if_key_is_expired() throws Exception {
    try {
      User user = User.from(
              Username.from("john@doe.fr"),
              Password.from("hXYHz1OSnuod1SuvLcgD4A=="),
              "aaa",
              LocalDateTime.of(2017, 12, 10, 12, 0));
      given(userRepository.findUserFromCredentials(any(Credentials.class))).willReturn(user);
      ChangePassword changePassword = ChangePassword.from("aaa", "password", "newpassword");

      userOrganisationImpl.changePassword(credentials, changePassword);
      fail("Should throw invalid change password key exception");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Invalid token to change password");
    }
  }

  @Test
  public void should_update_profile_of_collaborator() throws Exception {
    given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("john@doe.fr"), null, null, null, null, null, null));
    Profile profileToUpdate = Profile.from(
            Username.from("john@doe.fr"),
            Name.of("John"),
            Name.of("Doe"),
            Email.from("john@doe.fr"),
            Link.to("www.johndoe.fr"),
            Link.to("github.com/johndoe"),
            Link.to("linkedin.com/johndoe"));
    given(userRepository.updateProfileOf(any(Profile.class))).willReturn(profileToUpdate);

    BaseProfile baseProfile = userOrganisationImpl.updateProfile(profileToUpdate);

    Profile expectedProfile = Profile.from(
            Username.from("john@doe.fr"),
            Name.of("John"),
            Name.of("Doe"),
            Email.from("john@doe.fr"),
            Link.to("www.johndoe.fr"),
            Link.to("github.com/johndoe"),
            Link.to("linkedin.com/johndoe"));
    assertThat(baseProfile).isEqualTo(expectedProfile);
  }

  @Test
  public void should_throw_profile_exception_if_email_is_invalid_when_updating_profile() throws Exception {
    try {
      given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("john@doe.fr"), null, null, null, null, null, null));
      BaseProfile baseProfileToUpdate = Profile.from(
              Username.from("john@doe.fr"),
              Name.of("John"),
              Name.of("Doe"),
              Email.from("john"),
              Link.to("www.johndoe.fr"),
              Link.to("github.com/johndoe"),
              Link.to("linkedin.com/johndoe"));

      userOrganisationImpl.updateProfile(baseProfileToUpdate);
      fail("Should have throw a baseProfile exception when email is invalid");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Invalid profile");
    }
  }

  @Test
  public void should_throw_collaborator_exception_if_profile_is_not_known() throws Exception {
    try {
      given(userRepository.findProfileOf(any(Username.class))).willReturn(new UnknownProfile());
      BaseProfile baseProfileToUpdate = Profile.from(
              Username.from("john@doe.fr"),
              Name.of("John"),
              Name.of("Doe"),
              Email.from("john@doe.fr"),
              Link.to("www.johndoe.fr"),
              Link.to("github.com/johndoe"),
              Link.to("linkedin.com/johndoe"));

      userOrganisationImpl.updateProfile(baseProfileToUpdate);
      fail("Should have throw a collaborator exception when email is invalid");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown collaborator");
    }
  }

  @Test
  public void should_find_email_of_collaborator_if_email_is_present() throws Exception {
    given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("john@doe.fr"), null, null, Email.from("johndoe@myapp.fr"), null, null, null));

    Email email = userOrganisationImpl.findEmailOf(credentials.getUsername());

    assertThat(email).isEqualTo(Email.from("johndoe@myapp.fr"));
  }

  @Test
  public void should_find_email_if_email_is_not_present_but_username_if_an_email() throws Exception {
    given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("john@doe.fr"), null, null, null, null, null, null));

    Email email = userOrganisationImpl.findEmailOf(credentials.getUsername());

    assertThat(email).isEqualTo(Email.from("john@doe.fr"));
  }

  @Test
  public void should_return_empty_email_if_no_email_is_found() throws Exception {
    given(userRepository.findProfileOf(any(Username.class))).willReturn(Profile.from(Username.from("johndoe"), null, null, null, null, null, null));
    Username badCredentials = Username.from("johndoe");

    Email email = userOrganisationImpl.findEmailOf(badCredentials);

    assertThat(email.isValid()).isFalse();
  }
}
