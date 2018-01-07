package fr.sharingcraftsman.user.domain.user;

public class UnknownProfile extends Profile {
  @Override
  public boolean isKnown() {
    return false;
  }
}
