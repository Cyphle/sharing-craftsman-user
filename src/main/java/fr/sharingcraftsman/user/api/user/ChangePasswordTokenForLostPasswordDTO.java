package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordTokenForLostPasswordDTO {
  private ChangePasswordTokenDTO changePasswordToken;
  private EmailDTO email;

  public ChangePasswordTokenForLostPasswordDTO() {
  }

  private ChangePasswordTokenForLostPasswordDTO(ChangePasswordToken changePasswordToken, Email email) {
    this.changePasswordToken = ChangePasswordTokenDTO.fromDomainToApi(changePasswordToken);
    this.email = EmailDTO.fromDomainToApi(email);
  }

  public ChangePasswordTokenDTO getChangePasswordToken() {
    return changePasswordToken;
  }

  public void setChangePasswordToken(ChangePasswordTokenDTO changePasswordToken) {
    this.changePasswordToken = changePasswordToken;
  }

  public EmailDTO getEmail() {
    return email;
  }

  public void setEmail(EmailDTO email) {
    this.email = email;
  }

  public static ChangePasswordTokenForLostPasswordDTO from(ChangePasswordToken changePasswordToken, Email email) {
    return new ChangePasswordTokenForLostPasswordDTO(changePasswordToken, email);
  }
}
