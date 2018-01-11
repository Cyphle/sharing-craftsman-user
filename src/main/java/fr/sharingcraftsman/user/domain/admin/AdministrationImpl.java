package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.exceptions.AlreadyExistingUserException;
import fr.sharingcraftsman.user.domain.user.AbstractUser;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministrationImpl implements Administration {
  // TODO To delete
  private UserForAdminRepository userForAdminRepository;
  private AdminUserRepository adminUserRepository;

  public AdministrationImpl(UserForAdminRepository userForAdminRepository, AdminUserRepository adminUserRepository) {
    this.userForAdminRepository = userForAdminRepository;
    this.adminUserRepository = adminUserRepository;
  }

  @Override
  public List<UserInfo> getAllUsers() {
    List<User> users = adminUserRepository.getAllUsers();
    List<Profile> profiles = adminUserRepository.getAllProfiles();
    List<TechnicalUserDetails> technicalUserDetails = adminUserRepository.getAllTechnicalUserDetails();

    List<UserInfo> userInfos = new ArrayList<>();

    users.forEach(user -> {
      Optional<Profile> profile = profiles.stream()
              .filter(p -> p.getUsername().equals(user.getUsername()))
              .findFirst();
      Optional<TechnicalUserDetails> technicalUserDetail = technicalUserDetails.stream()
              .filter(t -> t.getUsername().equals(user.getUsername()))
              .findFirst();

      if (profile.isPresent() && technicalUserDetail.isPresent()) {
        userInfos.add(UserInfo.from(user, profile.get(), technicalUserDetail.get()));
      }
    });

    return userInfos;
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
