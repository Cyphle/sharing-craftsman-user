package fr.sharingcraftsman.user.domain.authorization;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.*;
import java.util.stream.Collectors;

public class AuthorizationManagerImpl implements AuthorizationManager {
  private UserAuthorizationRepository userAuthorizationRepository;
  private AuthorizationRepository authorizationRepository;

  public AuthorizationManagerImpl(UserAuthorizationRepository userAuthorizationRepository, AuthorizationRepository authorizationRepository) {
    this.userAuthorizationRepository = userAuthorizationRepository;
    this.authorizationRepository = authorizationRepository;
  }

  @Override
  public Authorization getAuthorizationsOf(Username username) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    for (Group group : groups) {
      List<Role> roles = authorizationRepository.getRolesOf(group.getName());
      group.addRoles(roles);
    }
    return Authorization.get(groups);
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return authorizationRepository.getAllRolesWithTheirGroups();
  }

  @Override
  public void createNewGroupWithRoles(Group group) {
    Group foundGroup = authorizationRepository.getAllRolesWithTheirGroups()
            .stream()
            .filter(fetchedGroup -> fetchedGroup.getName().equals(group.getName()))
            .findFirst()
            .orElse(Group.from(""));

    List<Group> rolesToAdd = group.asSeparatedGroupByRole()
            .stream()
            .filter(role -> !foundGroup.getRoles().contains(Lists.newArrayList(role.getRoles()).get(0)))
            .collect(Collectors.toList());

    authorizationRepository.createNewGroupsWithRole(rolesToAdd);
  }

  @Override
  public void addGroup(Username username, Groups groupToAdd) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    if (doesNotAlreadyHaveGroup(groupToAdd, groups)) {
      userAuthorizationRepository.addGroupToCollaborator(username, groupToAdd);
    }
  }

  @Override
  public void removeGroup(Username username, Groups groupToRemove) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(username);
    if (hasGivenGroup(groupToRemove, groups)) {
      userAuthorizationRepository.removeGroupFromCollaborator(username, groupToRemove);
    }
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    Group filteredGroup = Group.from(group.getName(), new HashSet<>(Collections.singletonList(Lists.newArrayList(group.getRoles()).get(0))));
    authorizationRepository.removeRoleFromGroup(filteredGroup);
  }

  private boolean hasGivenGroup(Groups groupToRemove, List<Group> groups) {
    return groups.stream().anyMatch(group -> group.getName().equals(groupToRemove.name()));
  }

  private boolean doesNotAlreadyHaveGroup(Groups groupToAdd, List<Group> groups) {
    return groups.stream().noneMatch(group -> group.getName().equals(groupToAdd.name()));
  }
}
