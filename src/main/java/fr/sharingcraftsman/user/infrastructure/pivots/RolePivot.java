package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RolePivot {
  public static List<Role> fromInfraToDomain(List<GroupRole> groupRoles) {
    return groupRoles.stream()
            .map(groupRole -> new Role(groupRole.getRole()))
            .collect(Collectors.toList());
  }

  public static Set<Group> rolesWithGroupfromInfraToDomain(Iterable<GroupRole> roles) {
    Set<Group> groups = new HashSet<>();
    roles.forEach(role -> groups.add(new Group(role.getGroup())));
    roles.forEach(role -> {
      groups.forEach(group -> {
        if (role.getGroup().equals(group.getName()))
          group.addRole(new Role(role.getRole()));
      });
    });
    return groups;
  }
}
