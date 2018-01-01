package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.company.Person;

import java.util.List;

public interface HRAdminManager {
  List<AdminCollaborator> getAllCollaborators();

  void deleteCollaborator(Username username);

  Person findCollaboratorFromUsername(Username username);

  void updateCollaborator(AdminCollaborator collaborator);

  AdminCollaborator findAdminCollaboratorFromUsername(Username username);
}
