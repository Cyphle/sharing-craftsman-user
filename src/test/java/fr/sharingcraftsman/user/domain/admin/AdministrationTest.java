package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdministrationTest {
  @Mock
  private AdminUserRepository userRepository;

  private AdministrationImpl organisation;

  @Before
  public void setUp() throws Exception {
    organisation = new AdministrationImpl(userRepository);
  }

  @Test
  public void should_get_all_users_info() throws Exception {
    given(userRepository.getAllUsers()).willReturn(Arrays.asList(
            User.from("john@doe.fr", "password"),
            User.from("foo@bar.fr", "password")
    ));
    given(userRepository.getAllProfiles()).willReturn(Arrays.asList(
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe"), Name.of("picture.jpg")),
            Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"), Name.of("picture.jpg"))
    ));
    given(userRepository.getAllTechnicalUserDetails()).willReturn(Arrays.asList(
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0)),
            TechnicalUserDetails.from(Username.from("foo@bar.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    ));

    List<UserInfo> fetchedUserInfos = organisation.getAllUsers();

    verify(userRepository).getAllUsers();
    verify(userRepository).getAllProfiles();
    verify(userRepository).getAllTechnicalUserDetails();
    assertThat(fetchedUserInfos).isEqualTo(Arrays.asList(
            UserInfo.from(
                    User.from("john@doe.fr", "password"),
                    Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe"), Name.of("picture.jpg")),
                    TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))),
            UserInfo.from(
                    User.from("foo@bar.fr", "password"),
                    Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"), Name.of("picture.jpg")),
                    TechnicalUserDetails.from(Username.from("foo@bar.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0)))
    ));
  }

  @Test
  public void should_delete_user_if_exists() throws Exception {
    given(userRepository.findUserFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "password"));

    organisation.deleteUser(Username.from("hello@world.fr"));

    verify(userRepository).findUserFromUsername(Username.from("hello@world.fr"));
    verify(userRepository).deleteUser(Username.from("hello@world.fr"));
  }

  @Test
  public void should_throw_unknown_user_exception_if_user_not_found() throws Exception {
    given(userRepository.findUserFromUsername(any(Username.class))).willReturn(new UnknownUser());

    try {
      organisation.deleteUser(Username.from("hello@world.fr"));
      fail("Should have throw user exception when not found");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown user");
    }
  }

  @Test
  public void should_update_user() throws Exception {
    given(userRepository.findUserInfoFromUsername(Username.from("admin@toto.fr"))).willReturn(UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));

    organisation.updateUser(UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));

    verify(userRepository).findUserInfoFromUsername(Username.from("admin@toto.fr"));
    verify(userRepository).updateUser(UserInfo.from(
            User.from("admin@toto.fr", "T49xWf/l7gatvfVwethwDw=="),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));
  }

  @Test
  public void should_create_user() throws Exception {
    given(userRepository.findUserInfoFromUsername(any(Username.class))).willReturn(new UnknownUserInfo());

    organisation.createUser(UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));

    verify(userRepository).findUserInfoFromUsername(Username.from("admin@toto.fr"));
    verify(userRepository).createUser(UserInfo.from(
            User.from("admin@toto.fr", "T49xWf/l7gatvfVwethwDw=="),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    ));
  }
}
