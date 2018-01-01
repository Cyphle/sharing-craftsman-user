package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RoleDTO {
  private String role;

  public RoleDTO() {
  }

  public RoleDTO(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
