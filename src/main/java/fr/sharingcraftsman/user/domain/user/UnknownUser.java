package fr.sharingcraftsman.user.domain.user;

public class UnknownUser extends AbstractUser {
  @Override
  public boolean isKnown() {
    return false;
  }
}
