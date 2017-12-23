package fr.sharingcraftsman.user.domain.model;

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

  public String getEncryptedPassword() {
    return encryptedPassword;
  }
}
