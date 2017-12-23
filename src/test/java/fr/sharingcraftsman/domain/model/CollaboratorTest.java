package fr.sharingcraftsman.domain.model;

import fr.sharingcraftsman.user.domain.model.Collaborator;
import fr.sharingcraftsman.user.domain.model.Credentials;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CollaboratorTest {
  private Crypter crypter;

  @Before
  public void setUp() throws Exception {
    crypter = new AESCrypter();
  }

  @Test
  public void should_create_user_from_valid_credentials() throws Exception {
    Collaborator collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(crypter, "john@doe.fr", "password"));

    assertThat(collaborator).isNotNull();
  }
}
