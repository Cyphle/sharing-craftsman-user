package fr.sharingcraftsman.user.api.pivots;

import fr.sharingcraftsman.user.api.models.ChangePasswordDTO;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.company.ChangePassword;

public class ChangePasswordPivot {
  public static ChangePassword fromApiToDomain(ChangePasswordDTO changePasswordDTO) throws PasswordException {
    return ChangePassword.from(changePasswordDTO.getChangePasswordKey(), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
  }
}
