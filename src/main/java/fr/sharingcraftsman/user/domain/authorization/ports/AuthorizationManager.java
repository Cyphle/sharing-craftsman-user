package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.Set;

public interface AuthorizationManager {
  Authorization getAuthorizationsOf(Username username);

  void addGroup(Credentials credentials, Groups groupToAdd);

  Set<Group> getAllRolesWithTheirGroups();

  void removeGroup(Credentials credentials, Groups groupToRemove);

  void createNewGroupWithRoles(Group group);

  void removeRoleFromGroup(Group group);
}
