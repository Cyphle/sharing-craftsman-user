package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import io.swagger.annotations.ApiModelProperty;

public class LoginDTO {
  @ApiModelProperty(example = "client")
  private String username;
  @ApiModelProperty(example = "client")
  private String password;
  @ApiModelProperty(example = "true, false")
  private boolean persistentLogging;

  private LoginDTO() { }

  private LoginDTO(String username, String password) {
    this.username = username;
    this.password = password;
    this.persistentLogging = false;
  }

  private LoginDTO(String username, String password, boolean persistentLogging) {
    this.username = username;
    this.password = password;
    this.persistentLogging = persistentLogging;
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

  public boolean isPersistentLogging() {
    return persistentLogging;
  }

  public static LoginDTO from(String username, String password) {
    return new LoginDTO(username, password);
  }

  public static LoginDTO from(String username, String password, boolean persistentLogging) {
    return new LoginDTO(username, password, persistentLogging);
  }

  public static Credentials fromApiToDomain(LoginDTO loginDTO) throws CredentialsException {
    return Credentials.buildWithPersistentLogging(
            loginDTO.getUsername(),
            loginDTO.getPassword(),
            loginDTO.isPersistentLogging()
    );
  }
}
