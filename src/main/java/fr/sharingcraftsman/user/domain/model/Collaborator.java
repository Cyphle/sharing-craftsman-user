package fr.sharingcraftsman.user.domain.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Collaborator {
  private String username;
  private String password;

  public Collaborator(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public static Collaborator from(Credentials credentials) {
    Collaborator collaborator = new Collaborator(credentials.getUsername());
    collaborator.setPassword(credentials.getEncryptedPassword());
    return collaborator;
  }
}
