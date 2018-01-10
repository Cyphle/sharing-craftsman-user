package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.AbstractUserInfo;
import fr.sharingcraftsman.user.domain.admin.UserInfoOld;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.AbstractUser;

import java.util.List;

public interface UserForAdminRepository {
  List<UserInfoOld> getAllUsers();

  AbstractUser findUserFromUsername(Username username);

  AbstractUserInfo findAdminUserFromUsername(Username username);

  void createUser(UserInfoOld user);

  void updateUser(UserInfoOld user);

  void deleteUser(Username username);
}
