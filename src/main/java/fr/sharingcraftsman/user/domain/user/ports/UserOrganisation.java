package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.user.exceptions.InvalidChangePasswordTokenException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;

public interface UserOrganisation {
  Email findEmailOf(Username username);

  void createNewUser(Credentials credentials) throws UserException, CredentialsException;

  ChangePasswordToken createChangePasswordTokenFor(Username username) throws UnknownUserException, CredentialsException;

  void changePasswordOfUser(Username username, ChangePasswordInfo changePasswordInfo) throws UserException, CredentialsException;

  void changeLostPasswordOfUser(Username username, ChangePasswordInfo changePasswordInfo) throws UnknownUserException, CredentialsException, InvalidChangePasswordTokenException;

  AbstractProfile updateProfile(AbstractProfile abstractProfile) throws UserException;
}
