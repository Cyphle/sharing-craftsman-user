package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.BaseUser;

import java.util.List;

public interface HRAdminManager {
  List<AdminCollaborator> getAllCollaborators();

  void deleteCollaborator(Username username);

  BaseUser findCollaboratorFromUsername(Username username);

  void updateCollaborator(AdminCollaborator collaborator);

  AdminPerson findAdminCollaboratorFromUsername(Username username);

  void createCollaborator(AdminCollaborator collaborator);
}
