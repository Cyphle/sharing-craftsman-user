package fr.sharingcraftsman.command.adapters;

import fr.sharingcraftsman.user.command.adapters.UserAdapter;
import fr.sharingcraftsman.user.command.common.User;
import fr.sharingcraftsman.user.command.repositories.UserRepository;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserAdapterTest {
  @Mock
  private UserRepository userRepository;
  private HumanResourceAdministrator userAdapter;

  @Before
  public void setUp() throws Exception {
    userAdapter = new UserAdapter(userRepository);
  }

  @Test
  public void should_save_user_in_repository() throws Exception {
    Person collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password")));

    userAdapter.saveCollaborator((Collaborator) collaborator);

    verify(userRepository).save(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));
  }
}
