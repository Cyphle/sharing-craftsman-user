package fr.sharingcraftsman.user.domain.client;

public class AlreadyExistingClientException extends ClientException {
  public AlreadyExistingClientException(String message) {
    super(message);
  }
}
