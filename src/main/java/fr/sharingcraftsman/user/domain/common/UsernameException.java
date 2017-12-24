package fr.sharingcraftsman.user.domain.common;

import fr.sharingcraftsman.user.domain.authentication.CredentialException;

public class UsernameException extends CredentialException {
  public UsernameException(String message) {
    super(message);
  }
}
