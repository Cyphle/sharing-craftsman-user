package fr.sharingcraftsman.user.api.models;

import io.swagger.annotations.ApiModelProperty;

public class Login {
  @ApiModelProperty(example = "client")
  private String username;
  @ApiModelProperty(example = "client")
  private String password;
  @ApiModelProperty(example = "true, false")
  private boolean stayLogged;

  public Login() { }

  public Login(String username, String password) {
    this.username = username;
    this.password = password;
    this.stayLogged = false;
  }

  public Login(String username, String password, boolean stayLogged) {
    this.username = username;
    this.password = password;
    this.stayLogged = stayLogged;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean stayLogged() {
    return stayLogged;
  }
}
