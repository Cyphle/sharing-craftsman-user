package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;

public class Organisation {
  private HumanResourceAdministrator humanResourceAdministrator;

  public Organisation(HumanResourceAdministrator humanResourceAdministrator) {
    this.humanResourceAdministrator = humanResourceAdministrator;
  }

  public void createNewCollaborator(Credentials credentials) throws AlreadyExistingCollaborator {
    if (collaboratorExists(credentials.getUsername()))
      throw new AlreadyExistingCollaborator("Collaborator already exists with username: " + credentials.getUsernameContent());

    Collaborator newCollaborator = Collaborator.from(credentials);
    humanResourceAdministrator.createNewCollaborator(newCollaborator);
  }

  private boolean collaboratorExists(Username username) {
    return humanResourceAdministrator.getCollaborator(username).isKnown();
  }
}
