package fr.sharingcraftsman.user.domain.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Client extends BaseClient {
  private String name;
  private String secret;

  private Client(String name, String secret) {
    this.name = name;
    this.secret = secret;
  }

  public String getName() {
    return name;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public boolean isKnown() {
    return true;
  }

  public static Client from(String name, String secret) {
    return new Client(name, secret);
  }
}
