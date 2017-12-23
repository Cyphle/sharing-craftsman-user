package fr.sharingcraftsman.user.domain.model;

import fr.sharingcraftsman.user.domain.exceptions.PasswordException;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import fr.sharingcraftsman.user.domain.utils.CrypterFactory;

public class Password {
  public static PasswordBuilder passwordBuilder = new PasswordBuilder();
  public static Crypter crypter = CrypterFactory.getCrypter();

  private String password;

  public Password(String password) {
    this.password = password;
  }

  public Password getEncryptedVersion() {
    return new Password(crypter.encrypt(password));
  }

  public String getPassword() {
    return password;
  }

  public static class PasswordBuilder {
    public Password from(String password) throws PasswordException {
      if (password.isEmpty())
        throw new PasswordException("Password cannot be empty");

      return new Password(password);
    }
  }
}
