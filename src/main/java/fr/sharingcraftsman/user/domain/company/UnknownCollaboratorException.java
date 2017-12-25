package fr.sharingcraftsman.user.domain.company;

public class UnknownCollaboratorException extends CollaboratorException {
  public UnknownCollaboratorException(String message) {
    super(message);
  }
}
