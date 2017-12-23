package fr.sharingcraftsman.user.domain.model;

public interface HumanResourceAdministrator {
  void saveCollaborator(Collaborator collaborator);

  Person getCollaborator(Username username);
}
