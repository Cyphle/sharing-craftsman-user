package fr.sharingcraftsman.user.domain.user;

public class UnknownUser extends BaseUser {
  @Override
  public boolean isKnown() {
    return false;
  }
}
