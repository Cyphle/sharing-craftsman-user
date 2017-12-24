package fr.sharingcraftsman.user.command.pivots;

import fr.sharingcraftsman.user.command.common.User;
import fr.sharingcraftsman.user.domain.company.Collaborator;

public class UserPivot {
  public static User fromDomainToInfra(Collaborator collaborator) {
    return new User(collaborator.getUsername(), collaborator.getPassword());
  }
}
