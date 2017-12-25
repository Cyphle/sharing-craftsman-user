package fr.sharingcraftsman.user.domain.company;

public class UnkownCollaborator extends Person {
  @Override
  public boolean isKnown() {
    return false;
  }
}
