package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;

public class ChangePasswordTokenPivot {
  public static ChangePasswordToken fromDomainToApi(ChangePasswordKey changePasswordKey) {
    return new ChangePasswordToken(changePasswordKey.getKey());
  }
}
