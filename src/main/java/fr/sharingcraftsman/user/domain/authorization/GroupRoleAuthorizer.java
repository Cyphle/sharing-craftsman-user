package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.ports.authorization.Authorizer;

import java.util.List;

public class GroupRoleAuthorizer implements Authorizer {
  private GroupAdministrator groupAdministrator;
  private RoleAdministrator roleAdministrator;

  public GroupRoleAuthorizer(GroupAdministrator groupAdministrator, RoleAdministrator roleAdministrator) {
    this.groupAdministrator = groupAdministrator;
    this.roleAdministrator = roleAdministrator;
  }

  @Override
  public Authorizations getAuthorizationsOf(Credentials credentials) {
    /*
      -> get groups of user
      -> get roles linked to groups
      -> build authorizations
     */
    List<Group> groups = groupAdministrator.findGroupsOf(credentials.getUsername());
    for (Group group : groups) {
      List<Role> roles = roleAdministrator.getRolesOf(group.getName());
      group.addRoles(roles);
    }

    return new Authorizations(groups);
  }
}
