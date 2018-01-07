package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.GroupAdministrator;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.GroupPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserAuthorizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagerAdapter implements GroupAdministrator {
  private UserAuthorizationRepository userAuthorizationRepository;

  @Autowired
  public GroupManagerAdapter(UserAuthorizationRepository userAuthorizationRepository) {
    this.userAuthorizationRepository = userAuthorizationRepository;
  }

  @Override
  public List<Group> findGroupsOf(Username username) {
    return GroupPivot.fromInfraToDomain(userAuthorizationRepository.findByUsername(username.getUsername()));
  }

  @Override
  public void addGroupToCollaborator(Username username, Groups group) {
    userAuthorizationRepository.save(new UserAuthorizationEntity(username.getUsername(), group.name()));
  }

  @Override
  public void removeGroupFromCollaborator(Username username, Groups group) {
    UserAuthorizationEntity userAuthorizationEntity = userAuthorizationRepository.findByUsernameAndGroup(username.getUsername(), group.name());
    userAuthorizationRepository.delete(userAuthorizationEntity);
  }
}
