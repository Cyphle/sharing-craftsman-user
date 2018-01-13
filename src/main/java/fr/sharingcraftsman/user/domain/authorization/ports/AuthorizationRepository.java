package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;

import java.util.List;
import java.util.Set;

public interface AuthorizationRepository {
  List<Role> getRolesOf(String group);

  Set<Group> getAllRolesWithTheirGroups();

  void createNewGroupsWithRole(List<Group> groups);

  void removeRoleFromGroup(Group group);
}
