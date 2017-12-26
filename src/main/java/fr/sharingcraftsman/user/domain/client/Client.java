package fr.sharingcraftsman.user.domain.client;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Client {
  private String name;
  private String secret;
  private boolean isKnown;

  public Client(String name, String secret, boolean isKnown) {
    this.name = name;
    this.secret = secret;
    this.isKnown = isKnown;
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
    return isKnown;
  }

  public static Client from(String name, String secret) {
    return new Client(name, secret, false);
  }

  public static Client knownClient(String name, String secret) {
    return new Client(name, secret, true);
  }

  public static Client unkownClient() {
    return new Client("", "", false);
  }
}
