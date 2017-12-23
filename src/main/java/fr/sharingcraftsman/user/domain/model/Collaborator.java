package fr.sharingcraftsman.user.domain.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Collaborator extends Person {
  private Username username;
  private Password password;

  public Collaborator(Username username) {
    this.username = username;
  }

  public void setPassword(Password password) {
    this.password = password;
  }

  @Override
  public boolean isKnown() {
    return true;
  }

  public static Collaborator from(Credentials credentials) {
    Collaborator collaborator = new Collaborator(credentials.getUsername());
    collaborator.setPassword(credentials.getPassword());
    return collaborator;
  }
}
