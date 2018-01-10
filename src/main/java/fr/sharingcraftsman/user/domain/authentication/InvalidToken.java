package fr.sharingcraftsman.user.domain.authentication;

public class InvalidToken extends AbstractToken {
  @Override
  public boolean isValid() {
    return false;
  }
}
