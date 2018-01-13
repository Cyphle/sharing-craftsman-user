package fr.sharingcraftsman.user.infrastructure.models;

import fr.sharingcraftsman.user.domain.client.Client;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "clients")
@EqualsAndHashCode
public class ClientEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "name")
  private String name;
  @Column(name = "secret")
  private String secret;
  @Column(name = "is_active")
  private boolean isActive = true;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "last_update_date")
  private Date lastUpdateDate;

  private ClientEntity() {
  }

  private ClientEntity(String name, String secret) {
    this.name = name;
    this.secret = secret;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public static ClientEntity from(String name, String secret) {
    return new ClientEntity(name, secret);
  }

  public static ClientEntity fromDomainToInfra(Client client) {
    return ClientEntity.from(client.getName(), client.getSecret());
  }

  public static Client fromInfraToDomain(ClientEntity clientEntity) {
    return Client.from(clientEntity.getName(), clientEntity.getSecret());
  }
}
