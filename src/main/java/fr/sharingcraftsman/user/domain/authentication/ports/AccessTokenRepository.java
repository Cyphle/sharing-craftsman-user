package fr.sharingcraftsman.user.domain.authentication.ports;

import fr.sharingcraftsman.user.domain.authentication.AbstractToken;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.User;

public interface AccessTokenRepository {
  AbstractToken findTokenFromAccessToken(Client client, Username username, AccessToken token);

  AbstractToken findTokenFromRefreshToken(Client client, Username username, AccessToken token);

  AccessToken createNewToken(Client client, User user, AccessToken token);

  void deleteTokensOf(User user, Client client);
}
