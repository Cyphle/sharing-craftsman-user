package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Name {
  private static String name;

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
