package fr.sharingcraftsman.user.api.models;

public class ChangePasswordDTO {
  private String changePasswordKey;
  private String oldPassword;
  private String newPassword;

  public ChangePasswordDTO() {
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
