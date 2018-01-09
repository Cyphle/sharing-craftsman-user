package fr.sharingcraftsman.user.domain.user.exceptions;

public class InvalidChangePasswordTokenException extends UserException {
  public InvalidChangePasswordTokenException(String message) {
    super(message);
  }
}
