package fr.sharingcraftsman.user.api.models;

import fr.sharingcraftsman.user.api.pivots.ChangePasswordTokenPivot;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;

public class ChangePasswordKeyForLostPasswordDTO {
  private final ChangePasswordKeyDTO changePasswordKey;
  private final EmailDTO email;

  public ChangePasswordKeyForLostPasswordDTO(ChangePasswordKey changePasswordKey, Email email) {
    this.changePasswordKey = ChangePasswordTokenPivot.fromDomainToApi(changePasswordKey);
    this.email = EmailDTO.fromDomainToApi(email);
  }
}
