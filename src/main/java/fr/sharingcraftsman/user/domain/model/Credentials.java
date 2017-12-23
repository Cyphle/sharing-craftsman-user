package fr.sharingcraftsman.user.domain.model;

import fr.sharingcraftsman.user.domain.exceptions.CredentialException;
import fr.sharingcraftsman.user.domain.utils.Crypter;

public class Credentials {
  private Password password;
  private Username username;

  public Credentials(Username username, Password password) {
    this.username = username;
    this.password = password.getEncryptedVersion();
  }

  public String getUsername() {
    return username.getUsername();
  }

  public String getPassword() {
    return password.getPassword();
  }

  public static Credentials buildEncryptedCredentials(Crypter crypter, Username username, Password password) throws CredentialException {
    return new Credentials(username, password);
  }
}
