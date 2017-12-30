package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.common.Username;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagerAdapter implements GroupAdministrator {
  @Override
  public List<Group> findGroupsOf(Username username) {
    throw new UnsupportedOperationException();
  }
}
