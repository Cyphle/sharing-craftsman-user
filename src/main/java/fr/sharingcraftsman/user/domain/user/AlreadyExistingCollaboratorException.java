package fr.sharingcraftsman.user.domain.user;

public class AlreadyExistingCollaboratorException extends CollaboratorException {
  public AlreadyExistingCollaboratorException(String message) {
    super(message);
  }
}
