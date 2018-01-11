package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.UnknownUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdministrationTest {
  @Mock
  private UserForAdminRepository userForAdminRepository;
  @Mock
  private AdminUserRepository userRepository;

  private AdministrationImpl organisation;

  @Before
  public void setUp() throws Exception {
    organisation = new AdministrationImpl(userForAdminRepository, userRepository);
  }

  @Test
  public void should_get_all_users_info() throws Exception {
    given(userRepository.getAllUsers()).willReturn(Arrays.asList(
            User.from("john@doe.fr", "password"),
            User.from("foo@bar.fr", "password")
    ));
    given(userRepository.getAllProfiles()).willReturn(Arrays.asList(
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"))
    ));
    given(userRepository.getAllTechnicalUserDetails()).willReturn(Arrays.asList(
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0)),
            TechnicalUserDetails.from(Username.from("foo@bar.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    ));

    List<UserInfo> fetchedUserInfos = organisation.getAllUsers();

    User userOne = User.from("john@doe.fr", "password");
    Profile profileOne = Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe"));
    TechnicalUserDetails technicalDetailsOne = TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0));
    User userTwo = User.from("foo@bar.fr", "password");
    Profile profileTwo = Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"));
    TechnicalUserDetails technicalDetailsTwo = TechnicalUserDetails.from(Username.from("foo@bar.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0));
    List<UserInfo> expectedUserInfos = Arrays.asList(UserInfo.from(userOne, profileOne, technicalDetailsOne), UserInfo.from(userTwo, profileTwo, technicalDetailsTwo));
    verify(userRepository).getAllUsers();
    verify(userRepository).getAllProfiles();
    verify(userRepository).getAllTechnicalUserDetails();
    assertThat(fetchedUserInfos).isEqualTo(expectedUserInfos);
  }

  @Test
  public void should_delete_user_if_exists() throws Exception {
    given(userForAdminRepository.findUserFromUsername(Username.from("hello@world.fr"))).willReturn(User.from("hello@world.fr", "password"));

    organisation.deleteUser(Username.from("hello@world.fr"));

    verify(userForAdminRepository).findUserFromUsername(Username.from("hello@world.fr"));
    verify(userForAdminRepository).deleteUser(Username.from("hello@world.fr"));
  }

  @Test
  public void should_throw_unknown_user_exception_if_user_not_found() throws Exception {
    try {
      given(userForAdminRepository.findUserFromUsername(any(Username.class))).willReturn(new UnknownUser());
      organisation.deleteUser(Username.from("hello@world.fr"));
      fail("Should have throw user exception when not found");
    } catch (UserException e) {
      assertThat(e.getMessage()).isEqualTo("Unknown user");
    }
  }

  @Test
  public void should_update_user() throws Exception {
    UserInfoOld foundUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    given(userForAdminRepository.findAdminUserFromUsername(foundUser.getUsername())).willReturn(foundUser);

    UserInfoOld userToUpdate = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.updateUser(userToUpdate);

    UserInfoOld expectedUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminUserFromUsername(expectedUser.getUsername());
    verify(userForAdminRepository).updateUser(expectedUser);
  }

  @Test
  public void should_create_user() throws Exception {
    given(userForAdminRepository.findAdminUserFromUsername(any(Username.class))).willReturn(new UnknownUserInfo());

    UserInfoOld userToCreate = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    organisation.createUser(userToCreate);

    UserInfoOld expectedUser = UserInfoOld.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", true, new Date(), new Date());
    verify(userForAdminRepository).findAdminUserFromUsername(expectedUser.getUsername());
    verify(userForAdminRepository).createUser(expectedUser);
  }
}
