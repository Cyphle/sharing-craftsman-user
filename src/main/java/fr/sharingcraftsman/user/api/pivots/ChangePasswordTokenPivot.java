package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordKeyDTO;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public class ChangePasswordTokenPivot {
  public static ChangePasswordKeyDTO fromDomainToApi(ChangePasswordToken changePasswordToken) {
    return new ChangePasswordKeyDTO(changePasswordToken.getToken());
  }
}
