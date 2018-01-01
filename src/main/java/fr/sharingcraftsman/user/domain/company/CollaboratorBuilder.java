package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.common.Password;
import fr.sharingcraftsman.user.domain.common.Username;

import java.time.LocalDateTime;

public class CollaboratorBuilder {
  private Username username;
  private Password password;
  private String changePasswordKey = "";
  private LocalDateTime changePasswordKeyExpirationDate;

  public CollaboratorBuilder withUsername(Username username) {
    this.username = username;
    return this;
  }

  public CollaboratorBuilder withPassword(Password password) {
    this.password = password;
    return this;
  }

  public CollaboratorBuilder withChangePasswordKey(String changePasswordKey) {
    this.changePasswordKey = changePasswordKey;
    return this;
  }

  public CollaboratorBuilder withChangePasswordKeyExpirationDate(LocalDateTime changePasswordKeyExpirationDate) {
    this.changePasswordKeyExpirationDate = changePasswordKeyExpirationDate;
    return this;
  }

  public Collaborator build() {
    return new Collaborator(this.username, this.password, changePasswordKey, this.changePasswordKeyExpirationDate);
  }
}
