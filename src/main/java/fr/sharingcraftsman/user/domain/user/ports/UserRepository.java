package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;

public interface UserRepository {
  void createNewUser(User user);

  BaseUser findUserFromUsername(Username username);

  BaseUser findUserFromCredentials(Credentials credentials);

  void deleteChangePasswordKeyOf(Username username);

  ChangePasswordKey createChangePasswordKeyFor(ChangePasswordKey changePasswordKey);

  void updateUserPassword(User user);

  BaseProfile findProfileOf(Username username);

  BaseProfile updateProfileOf(Profile profileToUpdate);
}
