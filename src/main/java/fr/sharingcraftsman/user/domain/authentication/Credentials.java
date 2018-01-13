package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;

public class Credentials {
  private Password password;
  private Username username;
  private boolean persistentLogging;

  private Credentials(Username username, Password password, boolean persistentLogging) {
    this.username = username;
    this.password = password;
    this.persistentLogging = persistentLogging;
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

  public boolean isPersistentLogging() {
    return persistentLogging;
  }

  public void setPersistentLogging(boolean persistentLogging) {
    this.persistentLogging = persistentLogging;
  }

  public Credentials getEncryptedVersion() {
    return new Credentials(username, password.getEncryptedVersion(), persistentLogging);
  }

  public static Credentials buildWithEncryption(String username, String password) throws UsernameException, PasswordException {
    return new Credentials(Username.from(username), Password.from(password).getEncryptedVersion(), false);
  }

  public static Credentials buildWithEncryption(Username username, Password password) {
    return new Credentials(username, password.getEncryptedVersion(), false);
  }

  public static Credentials buildWithEncryption(String username, String password, boolean persistentLogging) throws PasswordException, UsernameException {
    return new Credentials(Username.from(username), Password.from(password).getEncryptedVersion(), persistentLogging);
  }

  public static Credentials build(String username, String password) throws UsernameException, PasswordException {
    return new Credentials(Username.from(username), Password.from(password), false);
  }

  public static Credentials buildWithPersistentLogging(String username, String password, boolean persistentLogging) throws PasswordException, UsernameException {
    return new Credentials(Username.from(username), Password.from(password), persistentLogging);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Credentials that = (Credentials) o;

    if (password != null ? !password.equals(that.password) : that.password != null) return false;
    return username != null ? username.equals(that.username) : that.username == null;
  }

  @Override
  public int hashCode() {
    int result = password != null ? password.hashCode() : 0;
    result = 31 * result + (username != null ? username.hashCode() : 0);
    return result;
  }
}
