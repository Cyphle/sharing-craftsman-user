package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.List;

public class UserAuthorizationManagerImpl implements UserAuthorizationManager {
  private UserAuthorizationRepository userAuthorizationRepository;
  private AuthorizationRepository authorizationRepository;

  public UserAuthorizationManagerImpl(UserAuthorizationRepository userAuthorizationRepository, AuthorizationRepository authorizationRepository) {
    this.userAuthorizationRepository = userAuthorizationRepository;
    this.authorizationRepository = authorizationRepository;
  }

  @Override
  public Authorization getAuthorizationsOf(Username username) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    groups.forEach(group -> group.addRoles(authorizationRepository.getRolesOf(group.getName())));
    return Authorization.get(groups);
  }

  @Override
  public void addGroupToUser(Username username, Groups groupToAdd) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    if (doesNotAlreadyHaveGroup(groupToAdd, groups)) {
      userAuthorizationRepository.addGroupToUser(username, groupToAdd);
    }
  }

  @Override
  public void removeGroupFromUser(Username username, Groups groupToRemove) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    if (hasGivenGroup(groupToRemove, groups)) {
      userAuthorizationRepository.removeGroupFromUser(username, groupToRemove);
    }
  }

  private boolean hasGivenGroup(Groups groupToRemove, List<Group> groups) {
    return groups.stream().anyMatch(group -> group.getName().equals(groupToRemove.name()));
  }

  private boolean doesNotAlreadyHaveGroup(Groups groupToAdd, List<Group> groups) {
    return groups.stream().noneMatch(group -> group.getName().equals(groupToAdd.name()));
  }
}
