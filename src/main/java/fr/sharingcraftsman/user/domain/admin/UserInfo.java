package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;

public class UserInfo extends AbstractUserInfo {
  private User user;
  private Profile profile;
  private TechnicalUserDetails technicalUserDetails;

  @Override
  public boolean isKnown() {
    return true;
  }
}
