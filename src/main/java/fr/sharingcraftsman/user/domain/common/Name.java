package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Name {
  private String name;

  public Name(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static Name of(String name) {
    return new Name(name);
  }
}
