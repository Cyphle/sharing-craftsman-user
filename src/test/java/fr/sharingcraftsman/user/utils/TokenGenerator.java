package fr.sharingcraftsman.user.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
  public static String generateToken(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    random.nextBytes(new byte[96]);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(new byte[96]);
  }
}
