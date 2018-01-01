package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ChangePasswordKeyDTO {
  private String token;

  public ChangePasswordKeyDTO(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
