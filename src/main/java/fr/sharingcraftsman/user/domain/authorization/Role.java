package fr.sharingcraftsman.user.domain.authorization;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Role {
  private String name;

  private Role(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Role from(String name) {
    return new Role(name);
  }
}
