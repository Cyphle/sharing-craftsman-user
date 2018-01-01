package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HRAdminAdapterTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private DateService dateService;
  private HRAdminAdapter hrAdminAdapter;

  @Before
  public void setUp() throws Exception {
    hrAdminAdapter = new HRAdminAdapter(userRepository, dateService);
    given(dateService.nowInDate()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
  }

  @Test
  public void should_get_all_collaborators() throws Exception {
    User user = new User("john@doe.fr", "password", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe");
    user.setActive(true);
    user.setCreationDate(new Date());
    user.setLastUpdateDate(new Date());
    user.setChangePasswordKey("");
    user.setChangePasswordExpirationDate(null);
    User userTwo = new User("foo@bar.fr", "password", "Foo", "Bar", "foo@bar.fr", "www.foobar.fr", "github.com/foobar", "linkedin.com/foobar");
    userTwo.setActive(true);
    userTwo.setCreationDate(new Date());
    userTwo.setLastUpdateDate(new Date());
    userTwo.setChangePasswordKey("");
    userTwo.setChangePasswordExpirationDate(null);
    given(userRepository.findAll()).willReturn(Arrays.asList(user, userTwo));

    hrAdminAdapter.getAllCollaborators();

    verify(userRepository).findAll();
  }

  @Test
  public void should_delete_user() throws Exception {
    hrAdminAdapter.deleteCollaborator(usernameBuilder.from("hello@world.fr"));

    verify(userRepository).delete(any(User.class));
  }

  @Test
  public void should_get_user_by_username() throws Exception {
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw=="));

    Person collaborator = hrAdminAdapter.findCollaboratorFromUsername(usernameBuilder.from("john@doe.fr"));

    Collaborator expected = Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false));
    assertThat((Collaborator) collaborator).isEqualTo(expected);
  }

  @Test
  public void should_get_user_by_username_in_admin_collaborator_object() throws Exception {
    User user = new User("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    user.setActive(true);
    user.setCreationDate(new Date());
    user.setLastUpdateDate(new Date());
    user.setChangePasswordKey("");
    user.setChangePasswordExpirationDate(null);
    given(userRepository.findByUsername("admin@toto.fr")).willReturn(user);

    AdminCollaborator collaborator = hrAdminAdapter.findAdminCollaboratorFromUsername(usernameBuilder.from("admin@toto.fr"));

    AdminCollaborator expectedCollaborator = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    assertThat(collaborator).isEqualTo(expectedCollaborator);
  }

  @Test
  public void should_update_user() throws Exception {
    User userToUpdate = new User("admin@toto.fr", "password", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    given(userRepository.findByUsername("admin@toto.fr")).willReturn(userToUpdate);

    AdminCollaborator collaboratorToUpdate = AdminCollaborator.from("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", "", null, true, new Date(), new Date());
    hrAdminAdapter.updateCollaborator(collaboratorToUpdate);

    User updatedUser = new User("admin@toto.fr", "password", "Admin", "Toto", "new@email.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto");
    updatedUser.setActive(true);
    updatedUser.setCreationDate(new Date());
    updatedUser.setLastUpdateDate(new Date());
    updatedUser.setChangePasswordKey("");
    updatedUser.setChangePasswordExpirationDate(null);
    verify(userRepository).save(updatedUser);
  }
}
