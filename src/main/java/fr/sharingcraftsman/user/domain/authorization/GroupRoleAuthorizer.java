package fr.sharingcraftsman.user.domain.authorization;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.ports.authorization.Authorizer;

import java.util.*;
import java.util.stream.Collectors;

public class GroupRoleAuthorizer implements Authorizer {
  private GroupAdministrator groupAdministrator;
  private RoleAdministrator roleAdministrator;

  public GroupRoleAuthorizer(GroupAdministrator groupAdministrator, RoleAdministrator roleAdministrator) {
    this.groupAdministrator = groupAdministrator;
    this.roleAdministrator = roleAdministrator;
  }

  @Override
  public Authorizations getAuthorizationsOf(Credentials credentials) {
    List<Group> groups = groupAdministrator.findGroupsOf(credentials.getUsername());
    for (Group group : groups) {
      List<Role> roles = roleAdministrator.getRolesOf(group.getName());
      group.addRoles(roles);
    }
    return new Authorizations(groups);
  }

  @Override
  public void addGroup(Credentials credentials, Groups groupToAdd) {
    List<Group> groups = groupAdministrator.findGroupsOf(credentials.getUsername());
    if (doesNotAlreadyHaveGroup(groupToAdd, groups)) {
      groupAdministrator.addGroupToCollaborator(credentials.getUsername(), groupToAdd);
    }
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return roleAdministrator.getAllRolesWithTheirGroups();
  }

  @Override
  public void removeGroup(Credentials credentials, Groups groupToRemove) {
    List<Group> groups = groupAdministrator.findGroupsOf(credentials.getUsername());
    if (hasGivenGroup(groupToRemove, groups)) {
      groupAdministrator.removeGroupFromCollaborator(credentials.getUsername(), groupToRemove);
    }
  }

  @Override
  public void createNewGroupWithRoles(Group group) {
    Group foundGroup = roleAdministrator.getAllRolesWithTheirGroups()
            .stream()
            .filter(fetchedGroup -> fetchedGroup.getName().equals(group.getName()))
            .findFirst()
            .orElse(new Group(""));

    List<Group> rolesToAdd = group.asSeparatedGroupByRole()
            .stream()
            .filter(role -> !foundGroup.getRoles().contains(Lists.newArrayList(role.getRoles()).get(0)))
            .collect(Collectors.toList());

    roleAdministrator.createNewGroupsWithRole(rolesToAdd);
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    Group filteredGroup = new Group(group.getName(), new HashSet<>(Collections.singletonList(Lists.newArrayList(group.getRoles()).get(0))));
    roleAdministrator.removeRoleFromGroup(filteredGroup);
  }

  private boolean hasGivenGroup(Groups groupToRemove, List<Group> groups) {
    return groups.stream().anyMatch(group -> group.getName().equals(groupToRemove.name()));
  }

  private boolean doesNotAlreadyHaveGroup(Groups groupToAdd, List<Group> groups) {
    return groups.stream().noneMatch(group -> group.getName().equals(groupToAdd.name()));
  }
}
