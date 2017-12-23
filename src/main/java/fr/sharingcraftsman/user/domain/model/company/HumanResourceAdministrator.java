package fr.sharingcraftsman.user.domain.model.company;

import fr.sharingcraftsman.user.domain.model.common.Username;

public interface HumanResourceAdministrator {
  void saveCollaborator(Collaborator collaborator);

  Person getCollaborator(Username username);
}
