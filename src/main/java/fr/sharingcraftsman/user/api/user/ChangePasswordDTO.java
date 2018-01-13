package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.user.ChangePasswordInfo;

public class ChangePasswordDTO {
  private String changePasswordToken;
  private String oldPassword;
  private String newPassword;

  private ChangePasswordDTO() {
  }

  private ChangePasswordDTO(String oldPassword, String newPassword) {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
  }

  private ChangePasswordDTO(String changePasswordToken, String oldPassword, String newPassword) {
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

  public static ChangePasswordDTO from(String oldPassword, String newPassword) {
    return new ChangePasswordDTO(oldPassword, newPassword);
  }

  public static ChangePasswordDTO from(String changePasswordToken, String oldPassword, String newPassword) {
    return new ChangePasswordDTO(changePasswordToken, oldPassword, newPassword);
  }

  public static ChangePasswordInfo fromApiToDomain(ChangePasswordDTO changePasswordDTO) throws PasswordException {
    return ChangePasswordInfo.from(changePasswordDTO.getChangePasswordToken(), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
  }
}
