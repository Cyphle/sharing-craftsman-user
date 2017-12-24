package fr.sharingcraftsman.user.domain.company;

public class AlreadyExistingCollaborator extends CollaboratorException {
  public AlreadyExistingCollaborator(String message) {
    super(message);
  }
}
