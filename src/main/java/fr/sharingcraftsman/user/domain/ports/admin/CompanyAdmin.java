package fr.sharingcraftsman.user.domain.ports.admin;

import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;

import java.util.List;

public interface CompanyAdmin {
  List<AdminCollaborator> getAllCollaborators();

  void deleteCollaborator(Username username) throws CollaboratorException;
}
