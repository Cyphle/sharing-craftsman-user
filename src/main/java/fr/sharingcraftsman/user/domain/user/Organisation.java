package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.ValidationError;
import fr.sharingcraftsman.user.domain.ports.company.Company;
import fr.sharingcraftsman.user.domain.utils.Crypter;
import fr.sharingcraftsman.user.domain.utils.CrypterFactory;

import java.util.List;

public class Organisation implements Company {
  private static Crypter crypter = CrypterFactory.getCrypter();
  private HumanResourceAdministrator humanResourceAdministrator;
  private DateService dateService;

  public Organisation(HumanResourceAdministrator humanResourceAdministrator, DateService dateService) {
    this.humanResourceAdministrator = humanResourceAdministrator;
    this.dateService = dateService;
  }

  @Override
  public void createNewCollaborator(Credentials credentials) throws CollaboratorException, CredentialsException {
    if (collaboratorExists(credentials.getUsername()))
      throw new AlreadyExistingCollaboratorException("User already exists with username: " + credentials.getUsernameContent());

    Credentials encryptedCredentials = Credentials.buildEncryptedCredentials(credentials.getUsername(), credentials.getPassword(), credentials.stayLogged());
    User newUser = User.from(encryptedCredentials);
    humanResourceAdministrator.createNewCollaborator(newUser);
  }

  @Override
  public ChangePasswordKey createChangePasswordKeyFor(Credentials credentials) throws UnknownCollaboratorException {
    if (!collaboratorExists(credentials.getUsername()))
      throw new UnknownCollaboratorException("Unknown collaborator");

    humanResourceAdministrator.deleteChangePasswordKeyOf(credentials);
    ChangePasswordKey changePasswordKey = ChangePasswordKey.from(
            (new CollaboratorBuilder())
                    .withUsername(credentials.getUsername())
                    .withPassword(null)
                    .build(),
            crypter.encrypt(credentials.getUsernameContent()),
            dateService.getDayAt(1)
    );
    return humanResourceAdministrator.createChangePasswordKeyFor(changePasswordKey);
  }

  @Override
  public void changePassword(Credentials credentials, ChangePassword changePassword) throws CollaboratorException {
    BaseUser baseUser = humanResourceAdministrator.findCollaboratorFromCredentials(credentials);

    if (!baseUser.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    checkChangePasswordKeyValidity(changePassword, (User) baseUser);

    ((User) baseUser).setPassword(changePassword.getNewPassword().getEncryptedVersion());
    humanResourceAdministrator.updateCollaboratorPassword((User) baseUser);
    humanResourceAdministrator.deleteChangePasswordKeyOf(credentials);
  }

  @Override
  public KnownProfile updateProfile(Profile profile) throws CollaboratorException {
    Profile profileToUpdate = humanResourceAdministrator.findProfileOf(((KnownProfile) profile).getUsername());

    if (!profileToUpdate.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    List<ValidationError> errors = ((KnownProfile) profile).validate();
    if (!errors.isEmpty())
      throw new ProfileException("Invalid profile", errors);

    ((KnownProfile) profileToUpdate).updateFrom((KnownProfile) profile);
    return (KnownProfile) humanResourceAdministrator.updateProfileOf((KnownProfile) profileToUpdate);
  }

  @Override
  public Email findEmailOf(Credentials credentials) {
    Profile profile = humanResourceAdministrator.findProfileOf(credentials.getUsername());

    KnownProfile knownProfile = (KnownProfile) profile;
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
    return humanResourceAdministrator.findCollaboratorFromUsername(username).isKnown();
  }
}
