package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;

public interface UserOrganisation {
  void createNewCollaborator(Credentials credentials) throws UserException, CredentialsException;

  ChangePasswordToken createChangePasswordTokenFor(Username username) throws UnknownUserException, CredentialsException;

  void changePassword(Username username, ChangePasswordInfo changePasswordInfo) throws UserException, CredentialsException;

  AbstractProfile updateProfile(AbstractProfile abstractProfile) throws UserException;

  Email findEmailOf(Username username);
}
