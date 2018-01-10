package fr.sharingcraftsman.user.api.models;

import fr.sharingcraftsman.user.api.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordTokenForLostPasswordDTO {
  private final ChangePasswordTokenDTO changePasswordToken;
  private final EmailDTO email;

  public ChangePasswordTokenForLostPasswordDTO(ChangePasswordToken changePasswordToken, Email email) {
    this.changePasswordToken = ChangePasswordTokenPivot.fromDomainToApi(changePasswordToken);
    this.email = EmailDTO.fromDomainToApi(email);
  }
}
