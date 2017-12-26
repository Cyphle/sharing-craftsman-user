package fr.sharingcraftsman.user.api.models;

public class Login {
  private String client;
  private String clientSecret;
  private String username;
  private String password;
  private boolean stayLogged;

  public Login() { }

  public Login(String client, String clientSecret, String username, String password) {
    this.client = client;
    this.clientSecret = clientSecret;
    this.username = username;
    this.password = password;
    this.stayLogged = false;
  }

  public Login(String client, String clientSecret, String username, String password, boolean stayLogged) {
    this.client = client;
    this.clientSecret = clientSecret;
    this.username = username;
    this.password = password;
    this.stayLogged = stayLogged;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
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
