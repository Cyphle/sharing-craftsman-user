package fr.sharingcraftsman.user.domain.model.authentication;

import fr.sharingcraftsman.user.domain.exceptions.authentication.CredentialException;
import fr.sharingcraftsman.user.domain.model.common.Password;
import fr.sharingcraftsman.user.domain.model.common.Username;
import fr.sharingcraftsman.user.domain.utils.Crypter;

public class Credentials {
  private Password password;
  private Username username;

  public Credentials(Username username, Password password) {
    this.username = username;
    this.password = password.getEncryptedVersion();
  }

  public Username getUsername() {
    return username;
  }

  public Password getPassword() {
    return password;
  }

  public String getUsernameContent() {
    return username.getUsername();
  }

  public static Credentials buildEncryptedCredentials(Crypter crypter, Username username, Password password) throws CredentialException {
    return new Credentials(username, password);
  }
}
