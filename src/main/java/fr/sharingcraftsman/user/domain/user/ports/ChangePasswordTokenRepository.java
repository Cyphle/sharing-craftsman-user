package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;

public interface ChangePasswordTokenRepository {
  ChangePasswordToken findByUsername(Username username) throws CredentialsException;

  void deleteChangePasswordKeyOf(Username username);

  ChangePasswordToken createChangePasswordKeyFor(ChangePasswordToken changePasswordToken) throws CredentialsException;
}
