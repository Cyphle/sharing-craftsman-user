package fr.sharingcraftsman.domain.company;

import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Organisation;
import fr.sharingcraftsman.user.domain.company.UnkownCollaborator;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
  private Crypter crypter;
  private Organisation organisation;

  @Before
  public void setUp() throws Exception {
    crypter = new AESCrypter();
    organisation = new Organisation(humanResourceAdministrator);
  }

  @Test
  public void should_save_user_when_registering() throws Exception {
    given(humanResourceAdministrator.getCollaborator(any(Username.class))).willReturn(new UnkownCollaborator());

    Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));

    organisation.createNewCollaborator(credentials);

    verify(humanResourceAdministrator).createNewCollaborator(Collaborator.from(credentials));
  }

  @Test
  public void should_throw_collaborator_exception_if_user_already_exists() throws Exception {
    try {
      given(humanResourceAdministrator.getCollaborator(any(Username.class))).willReturn(
        new Collaborator(usernameBuilder.from("john@doe.fr"))
      );
      Credentials credentials = Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password"));

      organisation.createNewCollaborator(credentials);
      fail("Should throw CollaboratorException");
    } catch (CollaboratorException e) {
      assertThat(e.getMessage()).isEqualTo("Collaborator already exists with username: john@doe.fr");
    }
  }
}
