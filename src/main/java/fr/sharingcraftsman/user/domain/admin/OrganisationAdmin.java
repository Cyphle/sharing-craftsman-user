package fr.sharingcraftsman.user.domain.admin;

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
}
