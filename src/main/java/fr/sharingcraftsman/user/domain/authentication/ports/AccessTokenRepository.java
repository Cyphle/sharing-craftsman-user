package fr.sharingcraftsman.user.domain.authentication.ports;

import fr.sharingcraftsman.user.domain.authentication.BaseToken;
import fr.sharingcraftsman.user.domain.authentication.AccessToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.User;

public interface AccessTokenRepository {
  void deleteTokensOf(User user, Client client);

  AccessToken createNewToken(Client client, User user, AccessToken token);

  BaseToken findTokenFromAccessToken(Client client, Username username, AccessToken token);

  BaseToken findTokenFromRefreshToken(Client client, Username username, AccessToken token);
}
