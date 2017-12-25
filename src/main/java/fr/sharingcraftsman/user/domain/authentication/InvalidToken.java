package fr.sharingcraftsman.user.domain.authentication;

public class InvalidToken extends Token {
  @Override
  public boolean isValid() {
    return false;
  }
}
