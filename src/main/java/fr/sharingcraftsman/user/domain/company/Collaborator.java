package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Collaborator extends Person {
  public static CollaboratorBuilder collaboratorBuilder = new CollaboratorBuilder();
  private Username username;
  private Password password;

  public Collaborator(Username username) {
    this.username = username;
  }

  Collaborator(Username username, Password password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username.getUsername();
  }

  public String getPassword() {
    return password.getPassword();
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

  public static class CollaboratorBuilder {
    private Username username;
    private Password password;

    public CollaboratorBuilder withUsername(Username username) {
      this.username = username;
      return this;
    }

    public CollaboratorBuilder withPassword(Password password) {
      this.password = password;
      return this;
    }

    public Collaborator build() {
      return new Collaborator(this.username, this.password);
    }
  }
}
