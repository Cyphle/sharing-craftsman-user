package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;

import java.util.List;
import java.util.stream.Collectors;

public class RolePivot {
  public static List<Role> fromInfraToDomain(List<GroupRole> groupRoles) {
    return groupRoles.stream()
            .map(groupRole -> new Role(groupRole.getRole()))
            .collect(Collectors.toList());
  }
}
