package fr.sharingcraftsman.domain.model;

import fr.sharingcraftsman.user.domain.model.Credentials;
import fr.sharingcraftsman.user.domain.utils.AESCrypter;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CredentialTest {
  private Crypter crypter;

  @Before
  public void setUp() throws Exception {
    crypter = new AESCrypter();
  }

  @Test
  public void should_crypt_credentials_when_created() throws Exception {
    Credentials credentials = new Credentials(crypter, "john@doe.fr", "password");

    assertThat(credentials.getEncryptedPassword()).isEqualTo("T49xWf/l7gatvfVwethwDw==");
  }
}
