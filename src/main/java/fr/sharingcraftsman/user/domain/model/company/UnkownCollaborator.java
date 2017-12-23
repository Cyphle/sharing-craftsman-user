package fr.sharingcraftsman.user.domain.model.company;

import fr.sharingcraftsman.user.domain.model.company.Person;

public class UnkownCollaborator extends Person {
  @Override
  public boolean isKnown() {
    return false;
  }
}
