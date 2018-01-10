package fr.sharingcraftsman.user.domain.admin;

public class UnknownUserInfo extends AbstractUserInfo {
  @Override
  public boolean isKnown() {
    return false;
  }
}
