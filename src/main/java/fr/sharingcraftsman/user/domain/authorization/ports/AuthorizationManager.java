package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.Set;

public interface AuthorizationManager {
  Authorization getAuthorizationsOf(Username username);

  Set<Group> getAllRolesWithTheirGroups();

  void createNewGroupWithRoles(Group group);

  void addGroup(Username username, Groups groupToAdd);

  void removeGroup(Username username, Groups groupToRemove);

  void removeRoleFromGroup(Group group);
}
