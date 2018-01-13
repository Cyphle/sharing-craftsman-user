package fr.sharingcraftsman.user.api.client;

import fr.sharingcraftsman.user.domain.client.Client;

public class ClientDTO {
  private String name;
  private String secret;

  private ClientDTO() {
  }

  private ClientDTO(String name) {
    this.name = name;
  }

  private ClientDTO(String name, String secret) {
    this.name = name;
    this.secret = secret;
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

  public static ClientDTO from(String name) {
    return new ClientDTO(name);
  }

  public static ClientDTO from(String name, String secret) {
    return new ClientDTO(name, secret);
  }

  public static Client fromApiToDomain(ClientDTO clientDTO) {
    return Client.from(clientDTO.getName(), clientDTO.getSecret());
  }
}
