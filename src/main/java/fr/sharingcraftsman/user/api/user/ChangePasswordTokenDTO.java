package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ChangePasswordTokenDTO {
  private String token;

  private ChangePasswordTokenDTO(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public static ChangePasswordTokenDTO from(String token) {
    return new ChangePasswordTokenDTO(token);
  }

  public static ChangePasswordTokenDTO fromDomainToApi(ChangePasswordToken changePasswordToken) {
    return new ChangePasswordTokenDTO(changePasswordToken.getToken());
  }
}
