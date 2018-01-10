package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.domain.authorization.Role;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

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

  public static Set<RoleDTO> roleFromDomainToApi(Set<Role> roles) {
    return roles.stream()
            .map(role -> new RoleDTO(role.getName()))
            .collect(Collectors.toSet());
  }
}
