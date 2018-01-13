package fr.sharingcraftsman.user.infrastructure.models;

import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tokens")
public class AccessTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username")
  private String username;
  @Column(name = "client")
  private String client;
  @Column(name = "access_token")
  private String accessToken;
  @Column(name = "refresh_token")
  private String refreshToken;
  @Column(name = "expiration_date")
  private Date expirationDate;

  private AccessTokenEntity() {
  }

  private AccessTokenEntity(String client, String username, String accessToken, String refreshToken, Date expirationDate) {
    this.username = username;
    this.client = client;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expirationDate = expirationDate;
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

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }

  public static AccessTokenEntity from(String client, String username, String accessToken, String refreshToken, Date expirationDate) {
    return new AccessTokenEntity(client, username, accessToken, refreshToken, expirationDate);
  }

  public static AccessTokenEntity fromDomainToInfra(User user, Client client, AccessToken token) {
    return AccessTokenEntity.from(client.getName(), user.getUsernameContent(), token.getAccessToken(), token.getRefreshToken(), DateConverter.fromLocalDateTimeToDate(token.getExpirationDate()));
  }

  public static AccessToken fromInfraToDomain(AccessTokenEntity accessTokenEntity) {
    return AccessToken.from(accessTokenEntity.getAccessToken(), accessTokenEntity.getRefreshToken(), DateConverter.fromDateToLocalDateTime(accessTokenEntity.getExpirationDate()));
  }
}
