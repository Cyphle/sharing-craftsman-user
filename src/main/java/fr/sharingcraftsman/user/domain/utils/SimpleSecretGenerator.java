package fr.sharingcraftsman.user.domain.utils;

public class SimpleSecretGenerator implements SecretGenerator {
  @Override
  public String generateSecret() {
    return "secret";
  }
}
