package fr.sharingcraftsman.user.acceptance.dsl;

public class ChangePasswordDsl {
  private String changePasswordKey;
  private String oldPassword;
  private String newPassword;

  public ChangePasswordDsl() {
  }

  public ChangePasswordDsl(String changePasswordKey, String oldPassword, String newPassword) {
    this.changePasswordKey = changePasswordKey;
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  public String getChangePasswordKey() {
    return changePasswordKey;
  }

  public void setChangePasswordKey(String changePasswordKey) {
    this.changePasswordKey = changePasswordKey;
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
