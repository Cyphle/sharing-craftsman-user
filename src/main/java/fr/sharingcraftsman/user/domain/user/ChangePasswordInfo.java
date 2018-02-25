package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.PasswordException;

public class ChangePasswordInfo {
  private String changePasswordToken;
  private Password oldPassword;
  private final Password newPassword;

  private ChangePasswordInfo(String changePasswordToken, Password oldPassword, Password newPassword) {
    this.changePasswordToken = changePasswordToken;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public ChangePasswordInfo(String changePasswordToken, Password newPassword) {
    this.changePasswordToken = changePasswordToken;
    this.newPassword = newPassword;
  }

  public Password getNewPassword() {
    return newPassword;
  }

  public Password getOldPassword() {
    return oldPassword;
  }

  public static ChangePasswordInfo from(String changePasswordToken, String oldPassword, String newPassword) throws PasswordException {
    return new ChangePasswordInfo(changePasswordToken, Password.from(oldPassword), Password.from(newPassword));
  }

  public static ChangePasswordInfo from(String changePasswordToken, String newPassword) throws PasswordException {
    return new ChangePasswordInfo(changePasswordToken, Password.from(newPassword));
  }

  public String getChangePasswordToken() {
    return changePasswordToken;
  }
}
