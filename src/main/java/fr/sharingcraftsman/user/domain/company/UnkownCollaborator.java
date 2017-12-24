package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.company.Person;

public class UnkownCollaborator extends Person {
  @Override
  public boolean isKnown() {
    return false;
  }
}
