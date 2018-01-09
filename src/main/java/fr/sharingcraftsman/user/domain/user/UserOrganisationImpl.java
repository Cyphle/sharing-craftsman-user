package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.ValidationError;
import fr.sharingcraftsman.user.domain.user.exceptions.*;
import fr.sharingcraftsman.user.domain.user.ports.UserOrganisation;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import fr.sharingcraftsman.user.domain.utils.CrypterFactory;

import java.util.List;

public class UserOrganisationImpl implements UserOrganisation {
  private static Crypter crypter = CrypterFactory.getCrypter();
  private UserRepository userRepository;
  private DateService dateService;

  public UserOrganisationImpl(UserRepository userRepository, DateService dateService) {
    this.userRepository = userRepository;
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
  public ChangePasswordToken createChangePasswordTokenFor(Username username) throws UnknownUserException {
    if (!collaboratorExists(username))
      throw new UnknownUserException("Unknown collaborator");

    userRepository.deleteChangePasswordKeyOf(username);
    ChangePasswordToken changePasswordToken = ChangePasswordToken.from(
            User.from(username),
            crypter.encrypt(username.getUsername()),
            dateService.getDayAt(1)
    );
    return userRepository.createChangePasswordKeyFor(changePasswordToken);
  }

  @Override
  public void changePassword(Credentials credentials, ChangePassword changePassword) throws UserException {
    BaseUser baseUser = userRepository.findUserFromCredentials(credentials.getEncryptedVersion());

    if (!baseUser.isKnown())
      throw new UnknownUserException("Unknown collaborator");

    checkChangePasswordKeyValidity(changePassword, (User) baseUser);

    ((User) baseUser).setPassword(changePassword.getNewPassword().getEncryptedVersion());
    userRepository.updateUserPassword((User) baseUser);
    userRepository.deleteChangePasswordKeyOf(credentials.getUsername());
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

  private void checkChangePasswordKeyValidity(ChangePassword changePassword, User person) throws InvalidChangePasswordKeyException {
    if (!changePassword.getChangePasswordKey().equals(person.getChangePasswordKey()) || person.getChangePasswordKeyExpirationDate().isBefore(dateService.now()))
      throw new InvalidChangePasswordKeyException("Invalid token to change password");
  }

  private boolean collaboratorExists(Username username) {
    return userRepository.findUserFromUsername(username).isKnown();
  }
}
