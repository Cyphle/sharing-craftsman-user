package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class Group {
  Set<Role> roles;
  private String name;

  public Group(String name) {
    this.name = name;
    this.roles = new HashSet<>();
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
}
