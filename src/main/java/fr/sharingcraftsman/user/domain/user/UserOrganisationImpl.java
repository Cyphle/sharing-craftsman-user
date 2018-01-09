package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.ValidationError;
import fr.sharingcraftsman.user.domain.user.exceptions.*;
import fr.sharingcraftsman.user.domain.user.ports.ChangePasswordTokenRepository;
import fr.sharingcraftsman.user.domain.user.ports.UserOrganisation;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import fr.sharingcraftsman.user.domain.utils.CrypterFactory;

import java.util.List;

public class UserOrganisationImpl implements UserOrganisation {
  private static Crypter crypter = CrypterFactory.getCrypter();
  private UserRepository userRepository;
  private DateService dateService;
  private ChangePasswordTokenRepository changePasswordTokenRepository;

  public UserOrganisationImpl(UserRepository userRepository, ChangePasswordTokenRepository changePasswordTokenRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.changePasswordTokenRepository = changePasswordTokenRepository;
    this.dateService = dateService;
  }

  @Override
  public void createNewCollaborator(Credentials credentials) throws UserException, CredentialsException {
    if (collaboratorExists(credentials.getUsername()))
      throw new AlreadyExistingUserException("User already exists with username: " + credentials.getUsernameContent());

    Credentials encryptedCredentials = Credentials.buildWithEncryption(credentials.getUsernameContent(), credentials.getPasswordContent());
    User newUser = User.from(encryptedCredentials);
    userRepository.createNewUser(newUser);
  }

  @Override
  public ChangePasswordToken createChangePasswordTokenFor(Username username) throws UnknownUserException, CredentialsException {
    if (!collaboratorExists(username))
      throw new UnknownUserException("Unknown collaborator");

    changePasswordTokenRepository.deleteChangePasswordKeyOf(username);
    ChangePasswordToken changePasswordToken = ChangePasswordToken.from(
            User.from(username),
            crypter.encrypt(username.getUsername()),
            dateService.getDayAt(1)
    );
    return changePasswordTokenRepository.createChangePasswordKeyFor(changePasswordToken);
  }

  @Override
  public void changePassword(Username username, ChangePasswordInfo changePasswordInfo) throws UserException, CredentialsException {
    Credentials userCredentials = Credentials.buildWithEncryption(username, changePasswordInfo.getOldPassword());
    BaseUser baseUser = userRepository.findUserFromCredentials(userCredentials);

    if (!baseUser.isKnown())
      throw new UnknownUserException("Unknown collaborator");

    ChangePasswordToken token = changePasswordTokenRepository.findByUsername(username);
    checkChangePasswordKeyValidity(changePasswordInfo, token);

    ((User) baseUser).setPassword(changePasswordInfo.getNewPassword().getEncryptedVersion());
    userRepository.updateUserPassword((User) baseUser);
    changePasswordTokenRepository.deleteChangePasswordKeyOf(username);
  }

  @Override
  public Profile updateProfile(BaseProfile baseProfile) throws UserException {
    BaseProfile baseProfileToUpdate = userRepository.findProfileOf(((Profile) baseProfile).getUsername());

    if (!baseProfileToUpdate.isKnown())
      throw new UnknownUserException("Unknown collaborator");

    List<ValidationError> errors = ((Profile) baseProfile).validate();
    if (!errors.isEmpty())
      throw new ProfileValidationException("Invalid profile", errors);

    ((Profile) baseProfileToUpdate).updateFrom((Profile) baseProfile);
    return (Profile) userRepository.updateProfileOf((Profile) baseProfileToUpdate);
  }

  @Override
  public Email findEmailOf(Username username) {
    BaseProfile baseProfile = userRepository.findProfileOf(username);

    Profile knownProfile = (Profile) baseProfile;
    if (knownProfile.getEmail() != null && knownProfile.getEmail().isValid())
      return knownProfile.getEmail();

    Email emailFromUsername = Email.from(knownProfile.getUsernameContent());
    if (emailFromUsername.isValid())
      return emailFromUsername;

    return Email.from("");
  }

  private void checkChangePasswordKeyValidity(ChangePasswordInfo changePasswordInfo, ChangePasswordToken token) throws InvalidChangePasswordTokenException {
    if (!changePasswordInfo.getChangePasswordKey().equals(token.getToken()) || token.getExpirationDate().isBefore(dateService.now()))
      throw new InvalidChangePasswordTokenException("Invalid token to change password");
  }

  private boolean collaboratorExists(Username username) {
    return userRepository.findUserFromUsername(username).isKnown();
  }
}
