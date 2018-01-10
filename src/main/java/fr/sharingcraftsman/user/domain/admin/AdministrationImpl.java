package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;

import java.util.List;

public class AdministrationImpl implements Administration {
  private UserForAdminRepository userForAdminRepository;

  public AdministrationImpl(UserForAdminRepository userForAdminRepository) {
    this.userForAdminRepository = userForAdminRepository;
  }

  @Override
  public List<UserInfoOld> getAllUsers() {
    return userForAdminRepository.getAllUsers();
  }

  @Override
  public void createUser(UserInfoOld user) throws AlreadyExistingUserException {
    AbstractUserInfo foundUser = userForAdminRepository.findAdminUserFromUsername(user.getUsername());

    if (foundUser.isKnown())
      throw new AlreadyExistingUserException("User already exists with username: " + user.getUsernameContent());

    userForAdminRepository.createUser(user);
  }

  @Override
  public void updateUser(UserInfoOld user) throws UnknownUserException {
    AbstractUserInfo userToUpdate = userForAdminRepository.findAdminUserFromUsername(user.getUsername());

    if (!userToUpdate.isKnown())
      throw new UnknownUserException("Unknown user");

    ((UserInfoOld) userToUpdate).updateFields(user);
    userForAdminRepository.updateUser((UserInfoOld) userToUpdate);
  }

  @Override
  public void deleteUser(Username username) throws UserException {
    AbstractUser user = userForAdminRepository.findUserFromUsername(username);

    if (!user.isKnown())
      throw new UnknownUserException("Unknown user");

    userForAdminRepository.deleteUser(username);
  }
}
