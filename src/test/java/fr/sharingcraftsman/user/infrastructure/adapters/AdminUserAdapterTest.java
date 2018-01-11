package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AdminUserAdapterTest {
  @Mock
  private UserJpaRepository userJpaRepository;
  private AdminUserAdapter adminUserAdapter;

  @Before
  public void setUp() throws Exception {
    UserEntity userOne = new UserEntity(
            "john@doe.fr",
            "password",
            "John",
            "Doe",
            "john@doe.fr",
            "johndoe.fr",
            "github.com/johndoe",
            "linkedin.com/johndoe",
            true,
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)),
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)));
    UserEntity userTwo = new UserEntity(
            "foo@bar.fr",
            "password",
            "Foo",
            "Bar",
            "foo@bar.fr",
            "foobar.fr",
            "github.com/foobar",
            "linkedin.com/foobar",
            true,
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)),
            DateConverter.fromLocalDateTimeToDate(LocalDateTime.of(2017, Month.DECEMBER, 28, 12, 0)));

    given(userJpaRepository.findAll()).willReturn(Arrays.asList(userOne, userTwo));

    adminUserAdapter = new AdminUserAdapter(userJpaRepository);
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
            Profile.from(Username.from("john@doe.fr"), Name.of("John"), Name.of("Doe"), Email.from("john@doe.fr"), Link.to("johndoe.fr"), Link.to("github.com/johndoe"), Link.to("linkedin.com/johndoe")),
            Profile.from(Username.from("foo@bar.fr"), Name.of("Foo"), Name.of("Bar"), Email.from("foo@bar.fr"), Link.to("foobar.fr"), Link.to("github.com/foobar"), Link.to("linkedin.com/foobar"))
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
}
