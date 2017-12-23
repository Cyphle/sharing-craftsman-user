package fr.sharingcraftsman.user.domain.model;

public class UnkownCollaborator extends Person {
  @Override
  public boolean isKnown() {
    return false;
  }
}
