package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.common.Password;
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
  private AdminUserRepository adminUserRepository;

  public AdministrationImpl(AdminUserRepository adminUserRepository) {
    this.adminUserRepository = adminUserRepository;
  }

  @Override
  public List<UserInfo> getAllUsers() {
    List<User> users = adminUserRepository.getAllUsers();
    List<Profile> profiles = adminUserRepository.getAllProfiles();
    List<TechnicalUserDetails> technicalUserDetails = adminUserRepository.getAllTechnicalUserDetails();
    return mapUsersInfo(users, profiles, technicalUserDetails);
  }

  @Override
  public void createUser(UserInfo user) throws AlreadyExistingUserException {
    AbstractUserInfo foundUser = adminUserRepository.findUserInfoFromUsername(user.getUsername());

    if (foundUser.isKnown())
      throw new AlreadyExistingUserException("User already exists with username: " + user.getUsernameContent());

    user.setPassword(user.getPassword().getEncryptedVersion());
    adminUserRepository.createUser(user);
  }

  @Override
  public void updateUser(UserInfo user) throws UnknownUserException {
    AbstractUserInfo userToUpdate = adminUserRepository.findUserInfoFromUsername(user.getUsername());

    if (!userToUpdate.isKnown())
      throw new UnknownUserException("Unknown user");

    changeUserInfo(user, (UserInfo) userToUpdate);
    adminUserRepository.updateUser((UserInfo) userToUpdate);
  }

  @Override
  public void deleteUser(Username username) throws UserException {
    AbstractUser user = adminUserRepository.findUserFromUsername(username);

    if (!user.isKnown())
      throw new UnknownUserException("Unknown user");

    adminUserRepository.deleteUser(username);
  }

  private List<UserInfo> mapUsersInfo(List<User> users, List<Profile> profiles, List<TechnicalUserDetails> technicalUserDetails) {
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

  private void changeUserInfo(UserInfo user, UserInfo userToUpdate) {
    userToUpdate.updateFields(user);
    changeUserPassword(user, userToUpdate);
  }

  private void changeUserPassword(UserInfo user, UserInfo userToUpdate) {
    if (!user.getPasswordContent().equals("NOPASSWORD")) {
      Password newPassword = user.getPassword().getEncryptedVersion();
      Password oldPassword = userToUpdate.getPassword();

      if (!newPassword.equals(oldPassword)) {
        userToUpdate.setPassword(newPassword);
      }
    }
  }
}
