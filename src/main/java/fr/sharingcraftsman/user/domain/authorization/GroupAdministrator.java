package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.common.Username;

import java.util.List;

public interface GroupAdministrator {
  List<Group> findGroupsOf(Username username);

  void addGroupToCollaborator(Username username, Groups group);

  void removeGroupFromCollaborator(Username username, Groups group);
}
