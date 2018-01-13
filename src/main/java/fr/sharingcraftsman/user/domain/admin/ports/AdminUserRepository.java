package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.AbstractUserInfo;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;

import java.util.List;

public interface AdminUserRepository {
  AbstractUser findUserFromUsername(Username username);

  AbstractUserInfo findUserInfoFromUsername(Username username);

  List<User> getAllUsers();

  List<Profile> getAllProfiles();

  List<TechnicalUserDetails> getAllTechnicalUserDetails();

  void createUser(UserInfo user);

  void updateUser(UserInfo userToUpdate);

  void deleteUser(Username username);
}
