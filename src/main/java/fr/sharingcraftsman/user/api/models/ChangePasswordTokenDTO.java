package fr.sharingcraftsman.user.api.models;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ChangePasswordTokenDTO {
  private String token;

  public ChangePasswordTokenDTO(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
