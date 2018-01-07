package fr.sharingcraftsman.user.infrastructure.pivots;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GroupPivot {
  public static List<Group> fromInfraToDomain(List<UserAuthorizationEntity> userAuthorizationEntities) {
    return userAuthorizationEntities.stream()
            .map(group -> Group.from(group.getGroup()))
            .collect(Collectors.toList());
  }

  public static AuthorizationEntity fromDomainToInfra(Group group) {
    return new AuthorizationEntity(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getName());
  }
}
