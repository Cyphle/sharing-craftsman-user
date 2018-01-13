package fr.sharingcraftsman.user.domain.admin;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class TechnicalUserDetails {
  private Username username;
  private boolean isActive;
  private LocalDateTime creationDate;
  private LocalDateTime lastUpdateDate;

  public TechnicalUserDetails(Username username, boolean isActive, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
    this.username = username;
    this.isActive = isActive;
    this.creationDate = creationDate;
    this.lastUpdateDate = lastUpdateDate;
  }

  public Username getUsername() {
    return username;
  }

  public boolean isActive() {
    return isActive;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public LocalDateTime getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void updateFields(TechnicalUserDetails technicalUserDetails) {
    isActive = technicalUserDetails.isActive;
  }

  public static TechnicalUserDetails from(Username username, boolean isActive, LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
    return new TechnicalUserDetails(username, isActive, creationDate, lastUpdateDate);
  }

  public static TechnicalUserDetails fromInfraToDomain(UserEntity userEntity) throws UsernameException {
    return TechnicalUserDetails.from(
            Username.from(userEntity.getUsername()),
            userEntity.isActive(),
            DateConverter.fromDateToLocalDateTime(userEntity.getCreationDate()),
            DateConverter.fromDateToLocalDateTime(userEntity.getLastUpdateDate())
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TechnicalUserDetails that = (TechnicalUserDetails) o;

    if (isActive != that.isActive) return false;
    return username != null ? username.equals(that.username) : that.username == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (isActive ? 1 : 0);
    return result;
  }
}
