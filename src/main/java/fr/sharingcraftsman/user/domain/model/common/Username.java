package fr.sharingcraftsman.user.domain.model.common;

import fr.sharingcraftsman.user.domain.exceptions.common.UsernameException;

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
