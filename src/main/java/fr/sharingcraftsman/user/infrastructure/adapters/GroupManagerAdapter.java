package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserAuthorizationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupManagerAdapter implements UserAuthorizationRepository {
  private UserAuthorizationJpaRepository userAuthorizationJpaRepository;

  @Autowired
  public GroupManagerAdapter(UserAuthorizationJpaRepository userAuthorizationJpaRepository) {
    this.userAuthorizationJpaRepository = userAuthorizationJpaRepository;
  }

  @Override
  public List<Group> findGroupsOf(Username username) {
    return UserAuthorizationEntity.fromInfraToDomain(userAuthorizationJpaRepository.findByUsername(username.getUsername()));
  }

  @Override
  public void addGroupToUser(Username username, Groups group) {
    userAuthorizationJpaRepository.save(new UserAuthorizationEntity(username.getUsername(), group.name()));
  }

  @Override
  public void removeGroupFromUser(Username username, Groups group) {
    UserAuthorizationEntity userAuthorizationEntity = userAuthorizationJpaRepository.findByUsernameAndGroup(username.getUsername(), group.name());
    userAuthorizationJpaRepository.delete(userAuthorizationEntity);
  }
}
