package fr.sharingcraftsman.user.domain.exceptions;

public class AlreadyExistingCollaborator extends CollaboratorException {
  public AlreadyExistingCollaborator(String message) {
    super(message);
  }
}
