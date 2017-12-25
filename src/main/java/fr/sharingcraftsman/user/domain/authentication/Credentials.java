package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;

public class Credentials {
  private Password password;
  private Username username;
  private boolean stayLogged;

  public Credentials(Username username, Password password) {
    this.username = username;
    this.password = password.getEncryptedVersion();
    this.stayLogged = false;
  }

  public Credentials(Username username, Password password, boolean stayLogged) {
    this.username = username;
    this.password = password;
    this.stayLogged = stayLogged;
  }

  public Username getUsername() {
    return username;
  }

  public String getUsernameContent() {
    return username.getUsername();
  }

  public Password getPassword() {
    return password;
  }

  public String getPasswordContent() {
    return password.getPassword();
  }

  public boolean stayLogged() {
    return stayLogged;
  }

  public void setStayLogged(boolean stayLogged) {
    this.stayLogged = stayLogged;
  }

  public static Credentials buildEncryptedCredentials(Username username, Password password) throws CredentialsException {
    return new Credentials(username, password);
  }

  public static Credentials buildCredentials(Username username, Password password, boolean stayLogged) {
    return new Credentials(username, password, stayLogged);
  }
}
