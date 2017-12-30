package fr.sharingcraftsman.user.acceptance.dsl;

import java.util.List;

public class GroupDsl {
  private List<RoleDsl> roles;
  private String name;

  public List<RoleDsl> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDsl> roles) {
    this.roles = roles;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
