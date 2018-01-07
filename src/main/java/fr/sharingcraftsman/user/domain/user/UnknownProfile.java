package fr.sharingcraftsman.user.domain.user;

public class UnknownProfile extends BaseProfile {
  @Override
  public boolean isKnown() {
    return false;
  }
}
