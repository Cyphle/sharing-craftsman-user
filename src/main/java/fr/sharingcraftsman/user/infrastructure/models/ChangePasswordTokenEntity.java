package fr.sharingcraftsman.user.infrastructure.models;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.User;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "change_password_tokens")
@ToString
public class ChangePasswordTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "change_password_token")
  private String changePasswordToken;
  @Column(name = "change_password_expiration_date")
  private Date changePasswordExpirationDate;

  public ChangePasswordTokenEntity() {
  }

  public ChangePasswordTokenEntity(String username, String changePasswordToken, Date changePasswordExpirationDate) {
    this.username = username;
    this.changePasswordToken = changePasswordToken;
    this.changePasswordExpirationDate = changePasswordExpirationDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getChangePasswordToken() {
    return changePasswordToken;
  }

  public void setChangePasswordToken(String changePasswordToken) {
    this.changePasswordToken = changePasswordToken;
  }

  public Date getChangePasswordExpirationDate() {
    return changePasswordExpirationDate;
  }

  public void setChangePasswordExpirationDate(Date changePasswordExpirationDate) {
    this.changePasswordExpirationDate = changePasswordExpirationDate;
  }

  public static ChangePasswordToken fromInfraToDomain(User user, ChangePasswordTokenEntity token) {
    return ChangePasswordToken.from(user, token.getChangePasswordToken(), DateConverter.fromDateToLocalDateTime(token.getChangePasswordExpirationDate()));
  }

  public static ChangePasswordTokenEntity fromDomainToInfra(ChangePasswordToken changePasswordToken) {
    return new ChangePasswordTokenEntity(changePasswordToken.getUsername(), changePasswordToken.getToken(), DateConverter.fromLocalDateTimeToDate(changePasswordToken.getExpirationDate()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChangePasswordTokenEntity that = (ChangePasswordTokenEntity) o;

    if (username != null ? !username.equals(that.username) : that.username != null) return false;
    return changePasswordToken != null ? changePasswordToken.equals(that.changePasswordToken) : that.changePasswordToken == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (changePasswordToken != null ? changePasswordToken.hashCode() : 0);
    return result;
  }
}
