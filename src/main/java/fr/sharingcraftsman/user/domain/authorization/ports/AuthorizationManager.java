package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authorization.Group;

import java.util.Set;

public interface AuthorizationManager {
  Set<Group> getAllRolesWithTheirGroups();

  void createNewGroupWithRoles(Group group);

  void removeRoleFromGroup(Group group);
}
