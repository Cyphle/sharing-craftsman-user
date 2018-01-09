package fr.sharingcraftsman.user.api.models;

import fr.sharingcraftsman.user.api.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordTokenForLostPasswordDTO {
  private final ChangePasswordTokenDTO changePasswordKey;
  private final EmailDTO email;

  public ChangePasswordTokenForLostPasswordDTO(ChangePasswordToken changePasswordToken, Email email) {
    this.changePasswordKey = ChangePasswordTokenPivot.fromDomainToApi(changePasswordToken);
    this.email = EmailDTO.fromDomainToApi(email);
  }
}
