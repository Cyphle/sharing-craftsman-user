package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.common.Username;

import java.util.List;

public interface GroupAdministrator {
  List<Group> findGroupsOf(Username username);
}
