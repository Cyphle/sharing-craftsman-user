package fr.sharingcraftsman.user.api.client;

import fr.sharingcraftsman.user.domain.client.Client;

public class ClientDTO {
  private String name;
  private String secret;

  public ClientDTO() {
  }

  public ClientDTO(String name, String secret) {
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

  public static Client fromApiToDomain(ClientDTO clientDTO) {
    return Client.from(clientDTO.getName(), clientDTO.getSecret());
  }
}
