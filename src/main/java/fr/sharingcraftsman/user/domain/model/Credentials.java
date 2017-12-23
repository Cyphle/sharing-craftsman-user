package fr.sharingcraftsman.user.domain.model;

import fr.sharingcraftsman.user.domain.exceptions.CredentialException;
import fr.sharingcraftsman.user.domain.exceptions.PasswordException;
import fr.sharingcraftsman.user.domain.exceptions.UsernameException;
import fr.sharingcraftsman.user.domain.utils.Crypter;

public class Credentials {
  private String username;
  private String password;
  private final String encryptedPassword;

  public Credentials(Crypter crypter, String username, String password) {
    this.username = username;
    this.password = password;
    this.encryptedPassword = crypter.encrypt(password);
  }

  public String getUsername() {
    return username;
  }

  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  public static Credentials buildEncryptedCredentials(Crypter crypter, String username, String password) throws CredentialException {
    validateUsername(username);
    validatePassword(password);
    return new Credentials(crypter, username, password);
  }

  private static void validateUsername(String username) throws CredentialException {
    if (username.isEmpty())
      throw new UsernameException("Username cannot be empty");
  }

  private static void validatePassword(String password) throws PasswordException {
    if (password.isEmpty())
      throw new PasswordException("Password cannot be empty");
  }
}
