package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractProfile;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserEntityAdapterTest {
  @Mock
  private UserJpaRepository userJpaRepository;
  @Mock
  private DateService dateService;
  private UserRepository userAdapter;

  @Before
  public void setUp() throws Exception {
    userAdapter = new UserAdapter(userJpaRepository, dateService);
    given(dateService.nowInDate()).willReturn(DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0)));
  }

  @Test
  public void should_save_user_in_repository() throws Exception {
    userAdapter.createNewUser(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));

    verify(userJpaRepository).save(UserEntity.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw==", dateService.nowInDate(), dateService.nowInDate()));
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(UserEntity.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    AbstractUser user = userAdapter.findUserFromUsername(Username.from("john@doe.fr"));

    assertThat((User) user).isEqualTo(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));
  }

  @Test
  public void should_find_user_by_username_and_password() throws Exception {
    given(userJpaRepository.findByUsernameAndPassword("john@doe.fr", "T49xWf/l7gatvfVwethwDw==")).willReturn(UserEntity.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    AbstractUser user = userAdapter.findUserFromCredentials(Credentials.buildWithEncryption("john@doe.fr", "password"));

    assertThat((User) user).isEqualTo(User.from(Credentials.buildWithEncryption("john@doe.fr", "password")));
  }

  @Test
  public void should_update_user_with_new_password() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(UserEntity.from("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    userAdapter.updateUserPassword(User.from("john@doe.fr", "newpassword"));

    verify(userJpaRepository).save(UserEntity.from("john@doe.fr", "newpassword"));
  }

  @Test
  public void should_find_profile_from_username() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(UserEntity.from("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "picture.pjg"));

    AbstractProfile foundAbstractProfile = userAdapter.findProfileOf(Username.from("john@doe.fr"));

    assertThat((Profile) foundAbstractProfile).isEqualTo(Profile.from(
            Username.from("john@doe.fr"),
            Name.of("John"),
            Name.of("Doe"),
            Email.from("john@doe.fr"),
            Link.to("www.johndoe.fr"),
            Link.to("github.com/johndoe"),
            Link.to("linkedin.com/johndoe"),
            Name.of("picture.pjg"))
    );
    verify(userJpaRepository).findByUsername("john@doe.fr");
  }

  @Test
  public void should_save_new_profile() throws Exception {
    given(userJpaRepository.findByUsername("john@doe.fr")).willReturn(UserEntity.from("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "picture.pjg"));
    given(userJpaRepository.save(any(UserEntity.class))).willReturn(UserEntity.from("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", "picture.jpg"));

    AbstractProfile foundAbstractProfile = userAdapter.updateProfileOf(Profile.from(
            Username.from("john@doe.fr"),
            Name.of("John"),
            Name.of("Doe"),
            Email.from("john@doe.fr"),
            Link.to("www.johndoe.fr"),
            Link.to("github.com/johndoe"),
            Link.to("linkedin.com/johndoe"),
            Name.of("picture.jpg"))
    );

    assertThat((Profile) foundAbstractProfile).isEqualTo(Profile.from(
            Username.from("john@doe.fr"),
            Name.of("John"),
            Name.of("Doe"),
            Email.from("john@doe.fr"),
            Link.to("www.johndoe.fr"),
            Link.to("github.com/johndoe"),
            Link.to("linkedin.com/johndoe"),
            Name.of("picture.jpg"))
    );
    verify(userJpaRepository).findByUsername("john@doe.fr");
  }
}
