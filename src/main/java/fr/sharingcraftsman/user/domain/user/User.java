package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
public class User extends BaseUser {
  private Username username;
  private Password password;
  private String changePasswordKey;
  private LocalDateTime changePasswordKeyExpirationDate;

  public User(Username username) {
    this.username = username;
    this.changePasswordKey = "";
  }

  User(Username username, Password password) {
    this.username = username;
    this.password = password;
    changePasswordKey = "";
  }

  public User(Username username, Password password, String changePasswordKey, LocalDateTime changePasswordKeyExpirationDate) {
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

  public static User from(Credentials credentials) {
    User user = new User(credentials.getUsername());
    user.setPassword(credentials.getPassword());
    return user;
  }
}
