package fr.sharingcraftsman.user.api.models;

public class ClientRegistration {
  private String name;
  private String secret;

  public ClientRegistration() {
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
}
