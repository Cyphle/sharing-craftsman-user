package fr.sharingcraftsman.domain.model;

import fr.sharingcraftsman.user.domain.model.Collaborator;
import fr.sharingcraftsman.user.domain.model.Credentials;
import fr.sharingcraftsman.user.domain.model.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.model.Organisation;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    Credentials credentials = Credentials.buildEncryptedCredentials(crypter, "john@doe.fr", "password");

    organisation.createNewCollaborator(credentials);

    verify(humanResourceAdministrator).saveCollaborator(Collaborator.from(credentials));
  }
}
