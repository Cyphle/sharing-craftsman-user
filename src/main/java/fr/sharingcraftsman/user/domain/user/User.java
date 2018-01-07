package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
public class User extends BaseUser {
  private Username username;
  private Password password;
  private String changePasswordKey;
  private LocalDateTime changePasswordKeyExpirationDate;

  private User(Username username) {
    this.username = username;
    this.changePasswordKey = "";
  }

  private User(Username username, Password password) {
    this.username = username;
    this.password = password;
    changePasswordKey = "";
  }

  private User(Username username, Password password, String changePasswordKey, LocalDateTime changePasswordKeyExpirationDate) {
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

  public static User from(Username username) {
    return new User(username);
  }

  public static User from(String username, String password) throws UsernameException, PasswordException {
    return new User(Username.from(username), Password.from(password));
  }

  // TODO To delete changepasswordkey and changepasswordkeyexpierationdate
  public static User from(Username username, Password password, String changePasswordKey, LocalDateTime changePasswordKeyExpirationDate) {
    return new User(username, password, changePasswordKey, changePasswordKeyExpirationDate);
  }
}
