package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.domain.common.Email;

public class EmailDTO {
  private String email;

  public EmailDTO() {
  }

  public EmailDTO(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public static EmailDTO fromDomainToApi(Email email) {
    return new EmailDTO(email.getEmail());
  }
}
