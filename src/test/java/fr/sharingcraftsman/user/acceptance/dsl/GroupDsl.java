package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class GroupDsl {
  private Set<RoleDsl> roles;
  private String name;

  public GroupDsl() {
  }

  public GroupDsl(String name) {
    this.name = name;
    this.roles = new HashSet<>();
  }

  public Set<RoleDsl> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDsl> roles) {
    this.roles = roles;
  }

  public void addRole(RoleDsl role) {
    this.roles.add(role);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
