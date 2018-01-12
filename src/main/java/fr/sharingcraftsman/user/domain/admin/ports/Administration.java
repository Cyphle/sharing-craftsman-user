package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;

import java.util.List;

public interface Administration {
  List<UserInfo> getAllUsers();

  void createUser(UserInfo user) throws AlreadyExistingUserException;

  void updateUser(UserInfo user) throws UnknownUserException;

  void deleteUser(Username username) throws UserException;
}
