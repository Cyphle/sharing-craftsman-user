package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RoleDsl {
  private String role;

  public RoleDsl() {
  }

  public RoleDsl(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
