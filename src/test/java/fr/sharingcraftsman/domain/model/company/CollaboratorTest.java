package fr.sharingcraftsman.domain.model.company;

import fr.sharingcraftsman.user.domain.model.company.Collaborator;
import fr.sharingcraftsman.user.domain.model.authentication.Credentials;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;

import static fr.sharingcraftsman.user.domain.model.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.model.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class CollaboratorTest {
  private Crypter crypter;

  @Before
  public void setUp() throws Exception {
    crypter = new AESCrypter();
  }

  @Test
  public void should_create_user_from_valid_credentials() throws Exception {
    Collaborator collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(crypter, usernameBuilder.from("john@doe.fr"), passwordBuilder.from("password")));

    assertThat(collaborator).isNotNull();
  }
}
