package fr.sharingcraftsman.user.acceptance.dsl;

public class ChangePasswordDsl {
  private String changePasswordToken;
  private String oldPassword;
  private String newPassword;

  public ChangePasswordDsl() {
  }

  public ChangePasswordDsl(String changePasswordToken, String oldPassword, String newPassword) {
    this.changePasswordToken = changePasswordToken;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public String getChangePasswordToken() {
    return changePasswordToken;
  }

  public void setChangePasswordToken(String changePasswordToken) {
    this.changePasswordToken = changePasswordToken;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
