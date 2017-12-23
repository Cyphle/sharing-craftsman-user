package fr.sharingcraftsman.user.domain.exceptions.common;

import fr.sharingcraftsman.user.domain.exceptions.authentication.CredentialException;

public class PasswordException extends CredentialException {
  public PasswordException(String message) {
    super(message);
  }
}
