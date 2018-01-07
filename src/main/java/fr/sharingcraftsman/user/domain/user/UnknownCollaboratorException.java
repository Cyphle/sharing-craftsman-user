package fr.sharingcraftsman.user.domain.user;

public class UnknownCollaboratorException extends CollaboratorException {
  public UnknownCollaboratorException(String message) {
    super(message);
  }
}
