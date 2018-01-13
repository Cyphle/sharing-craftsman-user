package fr.sharingcraftsman.user.acceptance.dsl;

public class ClientDsl {
  private String name;
  private String secret;

  public ClientDsl() {
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
