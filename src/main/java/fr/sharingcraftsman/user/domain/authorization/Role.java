package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Role {
  private String role;

  public Role(String role) {
    this.role = role;
  }
}
