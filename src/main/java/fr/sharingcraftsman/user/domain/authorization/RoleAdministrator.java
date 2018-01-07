package fr.sharingcraftsman.user.domain.authorization;

import java.util.List;
import java.util.Set;

public interface RoleAdministrator {
  List<Role> getRolesOf(String group);

  Set<Group> getAllRolesWithTheirGroups();

  void createNewGroupsWithRole(List<Group> groups);

  void removeRoleFromGroup(Group group);
}
