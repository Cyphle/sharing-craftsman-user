package fr.sharingcraftsman.user.domain.authentication.ports;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.AbstractToken;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;

public interface AuthenticationManager {
  AbstractToken login(Client client, Credentials credentials) throws UserException;

  void logout(Client client, Username username, AccessToken token);

  boolean isTokenValid(Client client, Username username, AccessToken token);

  boolean isRefreshTokenValid(Client client, Username username, AccessToken token);

  AbstractToken createNewToken(Client client, Username username) throws UserException;

  void deleteToken(Client client, Username username);
}
