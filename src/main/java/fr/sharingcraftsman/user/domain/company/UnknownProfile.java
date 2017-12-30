package fr.sharingcraftsman.user.domain.company;

public class UnknownProfile extends Profile {
  @Override
  public boolean isKnown() {
    return false;
  }
}
