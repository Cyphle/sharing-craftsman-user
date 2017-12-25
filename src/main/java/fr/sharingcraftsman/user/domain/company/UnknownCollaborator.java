package fr.sharingcraftsman.user.domain.company;

public class UnknownCollaborator extends Person {
  @Override
  public boolean isKnown() {
    return false;
  }
}
