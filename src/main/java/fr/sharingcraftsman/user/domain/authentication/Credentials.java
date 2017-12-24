package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;

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

  public static Credentials buildEncryptedCredentials(Username username, Password password) throws CredentialException {
    return new Credentials(username, password);
  }
}
