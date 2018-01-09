package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordDTO;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.user.ChangePasswordInfo;

public class ChangePasswordPivot {
  public static ChangePasswordInfo fromApiToDomain(ChangePasswordDTO changePasswordDTO) throws PasswordException {
    return ChangePasswordInfo.from(changePasswordDTO.getChangePasswordKey(), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
  }
}
