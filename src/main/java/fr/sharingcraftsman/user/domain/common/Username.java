package fr.sharingcraftsman.user.domain.common;

import lombok.ToString;

@ToString
public class Username {
  private String username;

  public Username(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public static Username from(String username) throws UsernameException {
    if (username.isEmpty())
      throw new UsernameException("Username cannot be empty");

    return new Username(username);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Username username1 = (Username) o;

    return username != null ? username.equals(username1.username) : username1.username == null;
  }

  @Override
  public int hashCode() {
    return username != null ? username.hashCode() : 0;
  }
}
