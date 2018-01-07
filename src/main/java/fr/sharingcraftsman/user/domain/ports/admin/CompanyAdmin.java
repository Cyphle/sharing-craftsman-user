package fr.sharingcraftsman.user.domain.ports.admin;

import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AlreadyExistingCollaboratorException;
import fr.sharingcraftsman.user.domain.user.CollaboratorException;
import fr.sharingcraftsman.user.domain.user.UnknownCollaboratorException;

import java.util.List;

public interface CompanyAdmin {
  List<AdminCollaborator> getAllCollaborators();

  void deleteCollaborator(Username username) throws CollaboratorException;

  void updateCollaborator(AdminCollaborator collaborator) throws UnknownCollaboratorException;

  void createCollaborator(AdminCollaborator collaborator) throws AlreadyExistingCollaboratorException;
}
