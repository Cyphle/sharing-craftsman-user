package fr.sharingcraftsman.user.domain.user.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.*;

public interface UserRepository {
  AbstractUser findUserFromUsername(Username username);

  AbstractUser findUserFromCredentials(Credentials credentials);

  AbstractProfile findProfileOf(Username username);

  void createNewUser(User user);

  void updateUserPassword(User user);

  AbstractProfile updateProfileOf(Profile profileToUpdate);
}
