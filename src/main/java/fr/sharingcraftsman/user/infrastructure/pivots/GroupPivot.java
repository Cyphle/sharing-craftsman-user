package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.infrastructure.models.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

public class GroupPivot {
  public static List<Group> fromInfraToDomain(List<UserGroup> userGroups) {
    return userGroups.stream()
            .map(group -> new Group(group.getGroup()))
            .collect(Collectors.toList());
  }
}
