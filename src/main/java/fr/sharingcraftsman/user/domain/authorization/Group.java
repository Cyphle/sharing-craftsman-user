package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Group {
  List<Role> roles;
  private String name;

  public Group(String name) {
    this.name = name;
    this.roles = new ArrayList<>();
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

  public List<Role> getRoles() {
    return roles;
  }
}
