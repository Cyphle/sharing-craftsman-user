package fr.sharingcraftsman.user.domain.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Username {
  public static UsernameBuilder usernameBuilder = new UsernameBuilder();

  private String username;

  public Username(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public static class UsernameBuilder {
    public Username from(String username) throws UsernameException {
      if (username.isEmpty())
        throw new UsernameException("Username cannot be empty");

      return new Username(username);
    }
  }
}
