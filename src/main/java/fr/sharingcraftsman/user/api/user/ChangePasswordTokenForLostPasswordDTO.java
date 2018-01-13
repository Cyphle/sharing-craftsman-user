package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordTokenForLostPasswordDTO {
  private final ChangePasswordTokenDTO changePasswordToken;
  private final EmailDTO email;

  private ChangePasswordTokenForLostPasswordDTO(ChangePasswordToken changePasswordToken, Email email) {
    this.changePasswordToken = ChangePasswordTokenDTO.fromDomainToApi(changePasswordToken);
    this.email = EmailDTO.fromDomainToApi(email);
  }

  public static ChangePasswordTokenForLostPasswordDTO from(ChangePasswordToken changePasswordToken, Email email) {
    return new ChangePasswordTokenForLostPasswordDTO(changePasswordToken, email);
  }
}
