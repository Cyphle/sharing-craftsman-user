package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;

public interface UserRepository {
  void createNewUser(User user);

  AbstractUser findUserFromUsername(Username username);

  AbstractUser findUserFromCredentials(Credentials credentials);

  void updateUserPassword(User user);

  AbstractProfile findProfileOf(Username username);

  AbstractProfile updateProfileOf(Profile profileToUpdate);
}
