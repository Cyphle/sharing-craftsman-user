package fr.sharingcraftsman.user.domain.company;

import java.time.LocalDateTime;

public class ChangePasswordKey {
  private final Collaborator collaborator;
  private final String key;
  private LocalDateTime expirationDate;

  public ChangePasswordKey(Collaborator collaborator, String key, LocalDateTime expirationDate) {
    this.collaborator = collaborator;
    this.key = key;
    this.expirationDate = expirationDate;
  }

  public String getUsername() {
    return collaborator.getUsername();
  }

  public String getKey() {
    return key;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public static ChangePasswordKey from(Collaborator collaborator, String key, LocalDateTime expirationDate) {
    return new ChangePasswordKey(collaborator, key, expirationDate);
  }
}
