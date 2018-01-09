package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordTokenDTO;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;

public class ChangePasswordTokenPivot {
  public static ChangePasswordTokenDTO fromDomainToApi(ChangePasswordToken changePasswordToken) {
    return new ChangePasswordTokenDTO(changePasswordToken.getToken());
  }
}
