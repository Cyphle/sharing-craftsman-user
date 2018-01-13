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
  public Email findEmailOf(Username username) {
    AbstractProfile abstractProfile = userRepository.findProfileOf(username);

    Profile knownProfile = (Profile) abstractProfile;
    if (knownProfile.getEmail() != null && knownProfile.getEmail().isValid())
      return knownProfile.getEmail();

    Email emailFromUsername = Email.from(knownProfile.getUsernameContent());
    if (emailFromUsername.isValid())
      return emailFromUsername;

    return Email.from("");
  }

  @Override
  public void createNewUser(Credentials credentials) throws UserException, CredentialsException {
    if (doesUserExists(credentials.getUsername()))
      throw new AlreadyExistingUserException("User already exists with username: " + credentials.getUsernameContent());

    userRepository.createNewUser(User.from(credentials.getEncryptedVersion()));
  }

  @Override
  public ChangePasswordToken createChangePasswordTokenFor(Username username) throws UnknownUserException, CredentialsException {
    if (!doesUserExists(username))
      throw new UnknownUserException("Unknown user");

    changePasswordTokenRepository.deleteChangePasswordTokenOf(username);
    return changePasswordTokenRepository.createChangePasswordTokenFor(
            ChangePasswordToken.from(
                    User.from(username),
                    crypter.encrypt(username.getUsername()),
                    dateService.getDayAt(1)
            )
    );
  }

  @Override
  public void changePasswordOfUser(Username username, ChangePasswordInfo changePasswordInfo) throws UserException, CredentialsException {
    Credentials userCredentials = Credentials.buildWithEncryption(username, changePasswordInfo.getOldPassword());
    AbstractUser abstractUser = userRepository.findUserFromCredentials(userCredentials);

    if (!abstractUser.isKnown())
      throw new UnknownUserException("Unknown user");

    checkChangePasswordTokenValidity(changePasswordInfo, changePasswordTokenRepository.findByUsername(username));

    ((User) abstractUser).setPassword(changePasswordInfo.getNewPassword().getEncryptedVersion());
    userRepository.updateUserPassword((User) abstractUser);
    changePasswordTokenRepository.deleteChangePasswordTokenOf(username);
  }

  @Override
  public Profile updateProfile(AbstractProfile abstractProfile) throws UserException {
    AbstractProfile abstractProfileToUpdate = userRepository.findProfileOf(((Profile) abstractProfile).getUsername());

    if (!abstractProfileToUpdate.isKnown())
      throw new UnknownUserException("Unknown user");

    List<ValidationError> errors = ((Profile) abstractProfile).validate();
    if (!errors.isEmpty())
      throw new ProfileValidationException("Invalid profile", errors);

    ((Profile) abstractProfileToUpdate).updateFrom((Profile) abstractProfile);
    return (Profile) userRepository.updateProfileOf((Profile) abstractProfileToUpdate);
  }

  private void checkChangePasswordTokenValidity(ChangePasswordInfo changePasswordInfo, ChangePasswordToken token) throws InvalidChangePasswordTokenException {
    if (!changePasswordInfo.getChangePasswordToken().equals(token.getToken()) || token.getExpirationDate().isBefore(dateService.now()))
      throw new InvalidChangePasswordTokenException("Invalid token to change password");
  }

  private boolean doesUserExists(Username username) {
    return userRepository.findUserFromUsername(username).isKnown();
  }
}
