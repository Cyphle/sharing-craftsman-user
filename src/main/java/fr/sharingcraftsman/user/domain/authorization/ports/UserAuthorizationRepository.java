package fr.sharingcraftsman.user.domain.authorization.ports;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;

import java.util.List;

public interface UserAuthorizationRepository {
  List<Group> findGroupsOf(Username username);

  void addGroupToUser(Username username, Groups group);

  void removeGroupFromUser(Username username, Groups group);
}
