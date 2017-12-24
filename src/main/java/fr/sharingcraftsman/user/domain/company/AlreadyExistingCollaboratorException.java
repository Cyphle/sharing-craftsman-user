package fr.sharingcraftsman.user.domain.company;

public class AlreadyExistingCollaboratorException extends CollaboratorException {
  public AlreadyExistingCollaboratorException(String message) {
    super(message);
  }
}
