package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.Username;
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
}
