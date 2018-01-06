package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.ports.authorization.Authorizer;

import java.util.List;
import java.util.Set;

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
      groupAdministrator.addGroup(credentials.getUsername(), groupToAdd);
    }
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return roleAdministrator.getAllRolesWithTheirGroups();
  }

  private boolean doesNotAlreadyHaveGroup(Groups groupToAdd, List<Group> groups) {
    return groups.stream().noneMatch(group -> group.getName().equals(groupToAdd.name()));
  }

}
