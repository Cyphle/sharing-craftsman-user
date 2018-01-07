package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.PasswordException;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;

public class ChangePassword {
  private String changePasswordKey;
  private final Password oldPassword;
  private final Password newPassword;

  private ChangePassword(String changePasswordKey, Password oldPassword, Password newPassword) {
    this.changePasswordKey = changePasswordKey;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public Password getNewPassword() {
    return newPassword;
  }

  public static ChangePassword from(String changePasswordKey, String oldPassword, String newPassword) throws PasswordException {
    return new ChangePassword(changePasswordKey, passwordBuilder.from(oldPassword), passwordBuilder.from(newPassword));
  }

  public String getChangePasswordKey() {
    return changePasswordKey;
  }
}
