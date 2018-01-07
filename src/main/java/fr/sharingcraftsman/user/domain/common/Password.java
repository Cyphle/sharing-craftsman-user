package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.utils.Crypter;
import fr.sharingcraftsman.user.domain.utils.CrypterFactory;

public class Password {
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

  public static Password from(String password) throws PasswordException {
    if (password.isEmpty())
      throw new PasswordException("Password cannot be empty");

    return new Password(password);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Password password1 = (Password) o;

    return password != null ? password.equals(password1.password) : password1.password == null;
  }

  @Override
  public int hashCode() {
    return password != null ? password.hashCode() : 0;
  }
}
