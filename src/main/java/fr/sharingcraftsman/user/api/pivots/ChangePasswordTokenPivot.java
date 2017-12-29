package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordKeyDTO;
import fr.sharingcraftsman.user.domain.company.ChangePasswordKey;

public class ChangePasswordTokenPivot {
  public static ChangePasswordKeyDTO fromDomainToApi(ChangePasswordKey changePasswordKey) {
    return new ChangePasswordKeyDTO(changePasswordKey.getKey());
  }
}
