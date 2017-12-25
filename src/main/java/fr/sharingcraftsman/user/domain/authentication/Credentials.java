package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;

public class Credentials {
  private Password password;
  private Username username;
  private boolean extendedDuration;

  public Credentials(Username username, Password password) {
    this.username = username;
    this.password = password.getEncryptedVersion();
    this.extendedDuration = false;
  }

  public Credentials(Password password, Username username, boolean extendedDuration) {
    this.password = password;
    this.username = username;
    this.extendedDuration = extendedDuration;
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

  public void stayLogged(boolean stayLogged) {
    this.extendedDuration = stayLogged;
  }

  public static Credentials buildEncryptedCredentials(Username username, Password password) throws CredentialsException {
    return new Credentials(username, password);
  }
}
