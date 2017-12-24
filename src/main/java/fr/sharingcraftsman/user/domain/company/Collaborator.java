package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Collaborator extends Person {
  private Username username;
  private Password password;

  public Collaborator(Username username) {
    this.username = username;
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
}
