package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.ports.company.Company;

public class Organisation implements Company {
  private HumanResourceAdministrator humanResourceAdministrator;

  public Organisation(HumanResourceAdministrator humanResourceAdministrator) {
    this.humanResourceAdministrator = humanResourceAdministrator;
  }

  @Override
  public void createNewCollaborator(Credentials credentials) throws AlreadyExistingCollaboratorException {
    if (collaboratorExists(credentials.getUsername()))
      throw new AlreadyExistingCollaboratorException("Collaborator already exists with username: " + credentials.getUsernameContent());

    Collaborator newCollaborator = Collaborator.from(credentials);
    humanResourceAdministrator.createNewCollaborator(newCollaborator);
  }

  private boolean collaboratorExists(Username username) {
    return humanResourceAdministrator.getCollaborator(username).isKnown();
  }
}
