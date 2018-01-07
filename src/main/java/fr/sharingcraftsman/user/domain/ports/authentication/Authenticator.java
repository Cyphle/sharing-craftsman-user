package fr.sharingcraftsman.user.domain.ports.authentication;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.Token;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.CollaboratorException;

public interface Authenticator {
  Token login(Credentials credentials, Client client) throws CollaboratorException;

  boolean isTokenValid(Credentials credentials, Client client, ValidToken token);

  void logout(Credentials credentials, Client client, ValidToken token);

  boolean isRefreshTokenValid(Credentials credentials, Client client, ValidToken token);

  void deleteToken(Credentials credentials, Client client, ValidToken token);

  Token createNewToken(Credentials credentials, Client client) throws CollaboratorException;
}
