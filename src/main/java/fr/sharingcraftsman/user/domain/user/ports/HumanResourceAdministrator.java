package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;

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
