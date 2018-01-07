package fr.sharingcraftsman.user.domain.admin.exceptions;

import fr.sharingcraftsman.user.domain.admin.BaseUserForAdmin;

public class UnknownBaseUserForAdminCollaborator extends BaseUserForAdmin {
  @Override
  public boolean isKnown() {
    return false;
  }
}
