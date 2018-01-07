package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RolePivot {
  public static List<Role> fromInfraToDomain(List<AuthorizationEntity> authorizationEntities) {
    return authorizationEntities.stream()
            .map(groupRole -> Role.from(groupRole.getRole()))
            .collect(Collectors.toList());
  }

  public static Set<Group> fromInfraToDomainRolesGroupedByGroup(Iterable<AuthorizationEntity> roles) {
    Set<Group> groups = new HashSet<>();
    roles.forEach(role -> groups.add(Group.from(role.getGroup())));
    roles.forEach(role -> {
      groups.forEach(group -> {
        if (role.getGroup().equals(group.getName()))
          group.addRole(Role.from(role.getRole()));
      });
    });
    return groups;
  }
}
