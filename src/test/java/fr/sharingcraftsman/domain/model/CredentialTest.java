package fr.sharingcraftsman.domain.model;

import fr.sharingcraftsman.user.domain.exceptions.CredentialException;
import fr.sharingcraftsman.user.domain.model.Credentials;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CredentialTest {
  private Crypter crypter;

  @Before
  public void setUp() throws Exception {
    crypter = new AESCrypter();
  }

  @Test
  public void should_crypt_credentials_when_created() throws Exception {
    Credentials credentials = Credentials.buildEncryptedCredentials(crypter, "john@doe.fr", "password");

    assertThat(credentials.getEncryptedPassword()).isEqualTo("T49xWf/l7gatvfVwethwDw==");
  }

  @Test
  public void should_throw_invalid_username_when_username_is_empty() throws Exception {
    try {
      Credentials credentials = Credentials.buildEncryptedCredentials(crypter, "", "john@doe.fr");
      fail("Should have thrown a UsernameException");
    } catch (CredentialException e) {
      assertThat(e.getMessage()).isEqualTo("Username cannot be empty");
    }
  }

  @Test
  public void should_throw_invalid_password_when_password_is_empty() throws Exception {
    try {
      Credentials credentials = Credentials.buildEncryptedCredentials(crypter, "john@doe.fr", "");
      fail("Should have throws a PasswordException");
    } catch (CredentialException e) {
      assertThat(e.getMessage()).isEqualTo("Password cannot be empty");
    }
  }
}
