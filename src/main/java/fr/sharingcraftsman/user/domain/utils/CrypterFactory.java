package fr.sharingcraftsman.user.domain.utils;

public class CrypterFactory {
  public static Crypter getCrypter() {
    return new AESCrypter();
  }
}
