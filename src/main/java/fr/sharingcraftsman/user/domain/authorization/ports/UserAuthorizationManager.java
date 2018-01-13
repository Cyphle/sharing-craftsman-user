package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.Set;

public interface UserAuthorizationManager {
  Authorization getAuthorizationsOf(Username username);

  void addGroupToUser(Username username, Groups groupToAdd);

  void removeGroupFromUser(Username username, Groups groupToRemove);
}
