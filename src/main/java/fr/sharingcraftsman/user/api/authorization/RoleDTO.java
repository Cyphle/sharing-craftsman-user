package fr.sharingcraftsman.user.api.authorization;

import fr.sharingcraftsman.user.domain.authorization.Role;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class RoleDTO {
  private String name;

  private RoleDTO() {
  }

  private RoleDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static RoleDTO from(String name) {
    return new RoleDTO(name);
  }

  static Set<RoleDTO> roleFromDomainToApi(Set<Role> roles) {
    return roles.stream()
            .map(role -> RoleDTO.from(role.getName()))
            .collect(Collectors.toSet());
  }
}
