package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static fr.sharingcraftsman.user.domain.company.Collaborator.collaboratorBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAdapterTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private DateService dateService;
  private HumanResourceAdministrator userAdapter;

  @Before
  public void setUp() throws Exception {
    userAdapter = new UserAdapter(userRepository, dateService);
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void should_save_user_in_repository() throws Exception {
    Person collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false));

    userAdapter.createNewCollaborator((Collaborator) collaborator);

    User expectedUser = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUser.setCreationDate(dateService.nowInDate());
    expectedUser.setLastUpdateDate(dateService.nowInDate());
    verify(userRepository).save(expectedUser);
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    Person collaborator = userAdapter.getCollaborator(usernameBuilder.from("john@doe.fr"));

    assertThat((Collaborator) collaborator).isEqualTo(Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false)));
  }

  @Test
  public void should_find_user_by_username_and_password() throws Exception {
    given(userRepository.findByUsernameAndPassword("john@doe.fr", "T49xWf/l7gatvfVwethwDw==")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    Person collaborator = userAdapter.findFromCredentials(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false));

    assertThat((Collaborator) collaborator).isEqualTo(Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false)));
  }

  @Test
  public void should_delete_user_change_password_key() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    userAdapter.deleteChangePasswordKeyOf(Credentials.buildCredentials(usernameBuilder.from("john@doe.fr"), null, false));

    User user = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    verify(userRepository).save(user);
  }

  @Test
  public void should_update_user_with_change_password_key() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    ChangePasswordKey changePasswordKey = ChangePasswordKey.from(
            collaboratorBuilder
            .withUsername(usernameBuilder.from("john@doe.fr"))
            .withPassword(passwordBuilder.from("aaa"))
            .build(),
            "aaa",
            LocalDateTime.of(2017, 12, 25, 12, 0)
    );
    userAdapter.createChangePasswordKeyFor(changePasswordKey);

    User user = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    user.setChangePasswordKey("aaa");
    verify(userRepository).save(user);
  }
}
