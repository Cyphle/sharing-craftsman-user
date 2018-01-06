package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class Group {
  Set<Role> roles;
  private String name;

  public Group(String name) {
    this.name = name;
    this.roles = new HashSet<>();
  }

  public Group(String name, Set<Role> roles) {
    this.name = name;
    this.roles = roles;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void addRoles(List<Role> roles) {
    this.roles.addAll(roles);
  }

  public String getName() {
    return name;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public List<Group> asSeparatedGroupByRole() {
    return roles.stream()
            .map(role -> new Group(name, new HashSet<>(Collections.singletonList(role))))
            .collect(Collectors.toList());
  }
}
