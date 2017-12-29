package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrganisationTest {
  @Mock
  HumanResourceAdministrator humanResourceAdministrator;
  @Mock
  DateService dateService;
  private Organisation organisation;

  @Before
  public void setUp() throws Exception {
    organisation = new Organisation(humanResourceAdministrator, dateService);
  }

  @Test
  public void should_save_user_when_registering() throws Exception {
    given(humanResourceAdministrator.getCollaborator(any(Username.class))).willReturn(new UnknownCollaborator());

    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);

    organisation.createNewCollaborator(credentials);

    verify(humanResourceAdministrator).createNewCollaborator(Collaborator.from(credentials));
  }

  @Test
  public void should_throw_collaborator_exception_if_user_already_exists() throws Exception {
    try {
      given(humanResourceAdministrator.getCollaborator(any(Username.class))).willReturn(
        new Collaborator(usernameBuilder.from("john@doe.fr"))
      );
      Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"), false);

      organisation.createNewCollaborator(credentials);
      fail("Should throw CollaboratorException");
    } catch (CollaboratorException e) {
      assertThat(e.getMessage()).isEqualTo("Collaborator already exists with username: john@doe.fr");
    }
  }

  @Test
  public void should_delete_change_request_key_and_create_change_request_key() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 26, 12, 0));
    Credentials credentials = Credentials.buildCredentials(usernameBuilder.from("john@doe.fr"), null, false);

    organisation.createChangePasswordKeyFor(credentials);

    verify(humanResourceAdministrator).deleteChangePasswordKeyOf(credentials);
    verify(humanResourceAdministrator).createChangePasswordKeyFor(any(ChangePasswordKey.class));
  }
}
