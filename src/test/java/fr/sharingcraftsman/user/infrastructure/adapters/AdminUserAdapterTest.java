package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AbstractUserInfo;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminUserAdapterTest {
  @Mock
  private UserJpaRepository userJpaRepository;
  @Mock
  private DateService dateService;
  private AdminUserAdapter adminUserAdapter;

  @Before
  public void setUp() throws Exception {
    UserEntity userOne = UserEntity.from(
            "john@doe.fr",
            "password",
            "John",
            "Doe",
            "john@doe.fr",
            "johndoe.fr",
            "github.com/johndoe",
            "linkedin.com/johndoe",
            "picture.jpg",
            true,
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)),
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    );
    UserEntity userTwo = UserEntity.from(
            "foo@bar.fr",
            "password",
            "Foo",
            "Bar",
            "foo@bar.fr",
            "foobar.fr",
            "github.com/foobar",
            "linkedin.com/foobar",
            "picture.jpg",
            true,
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)),
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0))
    );

    given(userJpaRepository.findAll()).willReturn(Arrays.asList(userOne, userTwo));

    adminUserAdapter = new AdminUserAdapter(userJpaRepository, dateService);
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(UserEntity.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    AbstractUser user = adminUserAdapter.findUserFromUsername(Username.from("john@doe.fr"));

    User expected = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));
    assertThat((User) user).isEqualTo(expected);
  }

  @Test
  public void should_get_user_by_username_in_admin_user_object() throws Exception {
    UserEntity userEntity = UserEntity.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "picture.jpg");
    userEntity.setActive(true);
    userEntity.setCreationDate(new Date());
    userEntity.setLastUpdateDate(new Date());
    given(userJpaRepository.findByUsername("admin@toto.fr")).willReturn(userEntity);

    AbstractUserInfo user = adminUserAdapter.findUserInfoFromUsername(Username.from("admin@toto.fr"));

    UserInfo expectedUser = UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    );
    assertThat(user).isEqualTo(expectedUser);
  }

  @Test
  public void should_get_all_users() throws Exception {
    List<User> fetchedUsers = adminUserAdapter.getAllUsers();

    assertThat(fetchedUsers).containsExactly(
            User.from("john@doe.fr", "password"),
            User.from("foo@bar.fr", "password")
    );
  }

  @Test
  public void should_get_all_profiles() throws Exception {
    List<Profile> profiles = adminUserAdapter.getAllProfiles();

    assertThat(profiles).containsExactly(
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe"), Name.of("picture.jpg")),
            Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"), Name.of("picture.jpg"))
    );
  }

  @Test
  public void should_get_all_technical_user_details() throws Exception {
    List<TechnicalUserDetails> technicalUserDetails = adminUserAdapter.getAllTechnicalUserDetails();

    assertThat(technicalUserDetails).containsExactly(
            TechnicalUserDetails.from(Username.from("john@doe.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0)),
            TechnicalUserDetails.from(Username.from("foo@bar.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    );
  }

  @Test
  public void should_update_user() throws Exception {
    given(userJpaRepository.findByUsername("admin@toto.fr")).willReturn(UserEntity.from("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "picture.jpg"));

    adminUserAdapter.updateUser(UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    ));

    UserEntity updatedUserEntity = UserEntity.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "picture.jpg");
    updatedUserEntity.setActive(true);
    updatedUserEntity.setCreationDate(new Date());
    updatedUserEntity.setLastUpdateDate(new Date());
    verify(userJpaRepository).save(updatedUserEntity);
  }

  @Test
  public void should_create_user() throws Exception {
    UserInfo user = UserInfo.from(
            User.from("admin@toto.fr", "password"),
            Profile.from(Username.from("admin@toto.fr"), Name.of("Admin"), Name.of("Toto"), Email.from("new@email.fr"), Link.to("www.admintoto.fr"), Link.to("github.com/admintoto"), Link.to("linkedin.com/admintoto"), Name.of("picture.jpg")),
            TechnicalUserDetails.from(Username.from("admin@toto.fr"), true, LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0), LocalDateTime.of(2017, Month.DECEMBER, 29, 12, 0))
    );
    adminUserAdapter.createUser(user);

    UserEntity newUserEntity = UserEntity.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "picture.jpg");
    verify(userJpaRepository).save(newUserEntity);
  }

  @Test
  public void should_delete_user() throws Exception {
    adminUserAdapter.deleteUser(Username.from("hello@world.fr"));

    verify(userJpaRepository).delete(any(UserEntity.class));
  }
}
