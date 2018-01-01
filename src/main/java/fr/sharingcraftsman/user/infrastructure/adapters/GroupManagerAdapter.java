package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.infrastructure.models.UserGroup;
import fr.sharingcraftsman.user.infrastructure.pivots.GroupPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagerAdapter implements GroupAdministrator {
  private UserGroupRepository userGroupRepository;

  @Autowired
  public GroupManagerAdapter(UserGroupRepository userGroupRepository) {
    this.userGroupRepository = userGroupRepository;
  }

  @Override
  public List<Group> findGroupsOf(Username username) {
    return GroupPivot.fromInfraToDomain(userGroupRepository.findByUsername(username.getUsername()));
  }

  @Override
  public void addGroup(Username username, Groups group) {
    userGroupRepository.save(new UserGroup(username.getUsername(), group.name()));
  }
}
