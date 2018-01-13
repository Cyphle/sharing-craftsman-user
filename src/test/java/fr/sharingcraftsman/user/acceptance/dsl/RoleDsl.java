package fr.sharingcraftsman.user.acceptance.dsl;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class RoleDsl {
  private String name;

  public RoleDsl() {
  }

  public RoleDsl(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
