package fr.sharingcraftsman.user.domain.authorization;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;

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
  public Authorization getAuthorizationsOf(Credentials credentials) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(credentials.getUsername());
    for (Group group : groups) {
      List<Role> roles = authorizationRepository.getRolesOf(group.getName());
      group.addRoles(roles);
    }
    return new Authorization(groups);
  }

  @Override
  public void addGroup(Credentials credentials, Groups groupToAdd) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(credentials.getUsername());
    if (doesNotAlreadyHaveGroup(groupToAdd, groups)) {
      userAuthorizationRepository.addGroupToCollaborator(credentials.getUsername(), groupToAdd);
    }
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return authorizationRepository.getAllRolesWithTheirGroups();
  }

  @Override
  public void removeGroup(Credentials credentials, Groups groupToRemove) {
    List<Group> groups = userAuthorizationRepository.findGroupsOf(credentials.getUsername());
    if (hasGivenGroup(groupToRemove, groups)) {
      userAuthorizationRepository.removeGroupFromCollaborator(credentials.getUsername(), groupToRemove);
    }
  }

  @Override
  public void createNewGroupWithRoles(Group group) {
    Group foundGroup = authorizationRepository.getAllRolesWithTheirGroups()
            .stream()
            .filter(fetchedGroup -> fetchedGroup.getName().equals(group.getName()))
            .findFirst()
            .orElse(new Group(""));

    List<Group> rolesToAdd = group.asSeparatedGroupByRole()
            .stream()
            .filter(role -> !foundGroup.getRoles().contains(Lists.newArrayList(role.getRoles()).get(0)))
            .collect(Collectors.toList());

    authorizationRepository.createNewGroupsWithRole(rolesToAdd);
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    Group filteredGroup = new Group(group.getName(), new HashSet<>(Collections.singletonList(Lists.newArrayList(group.getRoles()).get(0))));
    authorizationRepository.removeRoleFromGroup(filteredGroup);
  }

  private boolean hasGivenGroup(Groups groupToRemove, List<Group> groups) {
    return groups.stream().anyMatch(group -> group.getName().equals(groupToRemove.name()));
  }

  private boolean doesNotAlreadyHaveGroup(Groups groupToAdd, List<Group> groups) {
    return groups.stream().noneMatch(group -> group.getName().equals(groupToAdd.name()));
  }
}
