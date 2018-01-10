package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.UserInfoOld;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;

import java.util.List;

public interface Administration {
  List<UserInfoOld> getAllUsers();

  void createUser(UserInfoOld user) throws AlreadyExistingUserException;

  void updateUser(UserInfoOld user) throws UnknownUserException;

  void deleteUser(Username username) throws UserException;
}
