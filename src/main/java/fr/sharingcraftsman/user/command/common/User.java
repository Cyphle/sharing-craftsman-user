package fr.sharingcraftsman.user.command.common;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@EqualsAndHashCode
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "password")
  private String password;
  @Column(name = "is_active")
  private boolean isActive;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "last_update_date")
  private Date lastUpdateDate;

  public User() { }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
