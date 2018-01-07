package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.AlreadyExistingCollaboratorException;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.Person;
import fr.sharingcraftsman.user.domain.company.UnknownCollaboratorException;
import fr.sharingcraftsman.user.domain.ports.admin.CompanyAdmin;

import java.util.List;

public class OrganisationAdmin implements CompanyAdmin {
  private HRAdminManager hrAdminManager;

  public OrganisationAdmin(HRAdminManager hrAdminManager) {
    this.hrAdminManager = hrAdminManager;
  }

  @Override
  public List<AdminCollaborator> getAllCollaborators() {
    return hrAdminManager.getAllCollaborators();
  }

  @Override
  public void deleteCollaborator(Username username) throws CollaboratorException {
    Person collaborator = hrAdminManager.findCollaboratorFromUsername(username);

    if (!collaborator.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    hrAdminManager.deleteCollaborator(username);
  }

  @Override
  public void updateCollaborator(AdminCollaborator collaborator) throws UnknownCollaboratorException {
    AdminPerson collaboratorToUpdate = hrAdminManager.findAdminCollaboratorFromUsername(collaborator.getUsername());

    if (!collaboratorToUpdate.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    ((AdminCollaborator) collaboratorToUpdate).updateFields(collaborator);
    hrAdminManager.updateCollaborator((AdminCollaborator) collaboratorToUpdate);
  }

  @Override
  public void createCollaborator(AdminCollaborator collaborator) throws AlreadyExistingCollaboratorException {
    AdminPerson foundCollaborator = hrAdminManager.findAdminCollaboratorFromUsername(collaborator.getUsername());

    if (foundCollaborator.isKnown())
      throw new AlreadyExistingCollaboratorException("Collaborator already exists with username: " + collaborator.getUsernameContent());

    hrAdminManager.createCollaborator(collaborator);
  }
}
