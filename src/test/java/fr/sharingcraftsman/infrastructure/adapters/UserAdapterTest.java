package fr.sharingcraftsman.infrastructure.adapters;

import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import fr.sharingcraftsman.user.infrastructure.adapters.UserAdapter;
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
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
    given(dateService.now()).willReturn(Date.from(LocalDateTime.of(2017, 12, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void should_save_user_in_repository() throws Exception {
    Person collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password")));

    userAdapter.createNewCollaborator((Collaborator) collaborator);

    User expectedUser = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUser.setCreationDate(dateService.now());
    expectedUser.setLastUpdateDate(dateService.now());
    verify(userRepository).save(expectedUser);
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    Person collaborator = userAdapter.getCollaborator(usernameBuilder.from("john@doe.fr"));

    assertThat((Collaborator) collaborator).isEqualTo(Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"))));
  }
}
