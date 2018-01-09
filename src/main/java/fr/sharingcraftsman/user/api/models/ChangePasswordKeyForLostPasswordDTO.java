package fr.sharingcraftsman.user.api.models;

import fr.sharingcraftsman.user.api.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordKeyForLostPasswordDTO {
  private final ChangePasswordKeyDTO changePasswordKey;
  private final EmailDTO email;

  public ChangePasswordKeyForLostPasswordDTO(ChangePasswordToken changePasswordToken, Email email) {
    this.changePasswordKey = ChangePasswordTokenPivot.fromDomainToApi(changePasswordToken);
    this.email = EmailDTO.fromDomainToApi(email);
  }
}
