package fr.sharingcraftsman.user.domain.admin;

public class UnknownAdminCollaborator extends AdminPerson {
  @Override
  public boolean isKnown() {
    return false;
  }
}
