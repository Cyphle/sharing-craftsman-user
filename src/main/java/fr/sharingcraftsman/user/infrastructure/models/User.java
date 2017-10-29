package fr.sharingcraftsman.user.infrastructure.models;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue
  private int id;
  @Column(name = "TOKEN")
  private String token;
  @Column(name = "PRINCIPALID")
  private String principalId;
  @Column(name = "FULLNAME")
  private String fullName;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "LOGINTYPE")
  @Enumerated(EnumType.STRING)
  private UserLoginType loginType;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(String principalId) {
    this.principalId = principalId;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserLoginType getLoginType() {
    return loginType;
  }

  public void setLoginType(UserLoginType loginType) {
    this.loginType = loginType;
  }
}
