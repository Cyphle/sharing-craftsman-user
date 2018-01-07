package fr.sharingcraftsman.user.domain.authentication.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.BaseToken;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;

public interface AuthenticationManager {
  BaseToken login(Credentials credentials, Client client) throws UserException;

  boolean isTokenValid(Credentials credentials, Client client, AccessToken token);

  void logout(Credentials credentials, Client client, AccessToken token);

  boolean isRefreshTokenValid(Credentials credentials, Client client, AccessToken token);

  void deleteToken(Credentials credentials, Client client, AccessToken token);

  BaseToken createNewToken(Credentials credentials, Client client) throws UserException;
}
