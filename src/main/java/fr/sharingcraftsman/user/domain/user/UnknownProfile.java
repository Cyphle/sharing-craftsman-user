package fr.sharingcraftsman.user.domain.user;

public class UnknownProfile extends AbstractProfile {
  @Override
  public boolean isKnown() {
    return false;
  }
}
