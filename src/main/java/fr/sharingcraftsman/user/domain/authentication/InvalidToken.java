package fr.sharingcraftsman.user.domain.authentication;

public class InvalidToken extends BaseToken {
  @Override
  public boolean isValid() {
    return false;
  }
}
