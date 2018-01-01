package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
public class Collaborator extends Person {
  private Username username;
  private Password password;
  private String changePasswordKey;
  private LocalDateTime changePasswordKeyExpirationDate;

  public Collaborator(Username username) {
    this.username = username;
    this.changePasswordKey = "";
  }

  Collaborator(Username username, Password password) {
    this.username = username;
    this.password = password;
    changePasswordKey = "";
  }

  public Collaborator(Username username, Password password, String changePasswordKey, LocalDateTime changePasswordKeyExpirationDate) {
    this(username, password);
    this.changePasswordKey = changePasswordKey;
    this.changePasswordKeyExpirationDate = changePasswordKeyExpirationDate;
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

  public String getChangePasswordKey() {
    return changePasswordKey;
  }

  public LocalDateTime getChangePasswordKeyExpirationDate() {
    return changePasswordKeyExpirationDate;
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
