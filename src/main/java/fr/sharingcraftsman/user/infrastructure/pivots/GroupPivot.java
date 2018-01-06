package fr.sharingcraftsman.user.infrastructure.pivots;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import fr.sharingcraftsman.user.infrastructure.models.UserGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupPivot {
  public static List<Group> fromInfraToDomain(List<UserGroup> userGroups) {
    return userGroups.stream()
            .map(group -> new Group(group.getGroup()))
            .collect(Collectors.toList());
  }

  public static GroupRole fromDomainToInfra(Group group) {
    return new GroupRole(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getRole());
  }
}
