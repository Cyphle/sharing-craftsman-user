package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;

public interface HumanResourceAdministrator {
  void createNewCollaborator(User user);

  BaseUser findCollaboratorFromUsername(Username username);

  BaseUser findCollaboratorFromCredentials(Credentials credentials);

  void deleteChangePasswordKeyOf(Credentials credentials);

  ChangePasswordKey createChangePasswordKeyFor(ChangePasswordKey changePasswordKey);

  void updateCollaboratorPassword(User user);

  BaseProfile findProfileOf(Username username);

  BaseProfile updateProfileOf(Profile profileToUpdate);
}
