package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.PasswordException;

public class ChangePasswordInfo {
  private String changePasswordKey;
  private final Password oldPassword;
  private final Password newPassword;

  private ChangePasswordInfo(String changePasswordKey, Password oldPassword, Password newPassword) {
    this.changePasswordKey = changePasswordKey;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public Password getNewPassword() {
    return newPassword;
  }

  public Password getOldPassword() {
    return oldPassword;
  }

  public static ChangePasswordInfo from(String changePasswordKey, String oldPassword, String newPassword) throws PasswordException {
    return new ChangePasswordInfo(changePasswordKey, Password.from(oldPassword), Password.from(newPassword));
  }

  public String getChangePasswordKey() {
    return changePasswordKey;
  }
}
