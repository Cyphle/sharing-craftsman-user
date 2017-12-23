package fr.sharingcraftsman.user.domain.model;

public class Organisation {
  private HumanResourceAdministrator humanResourceAdministrator;

  public Organisation(HumanResourceAdministrator humanResourceAdministrator) {
    this.humanResourceAdministrator = humanResourceAdministrator;
  }

  public void createNewCollaborator(Credentials credentials) {
    Collaborator newCollaborator = Collaborator.from(credentials);

    humanResourceAdministrator.saveCollaborator(newCollaborator);
  }
}
