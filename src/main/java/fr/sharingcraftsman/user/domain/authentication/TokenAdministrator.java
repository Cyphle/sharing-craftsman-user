package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;

public interface TokenAdministrator {
  void deleteTokensOf(User user, Client client);

  ValidToken createNewToken(Client client, User user, ValidToken token);

  Token findTokenFromAccessToken(Client client, Credentials credentials, ValidToken token);

  Token findTokenFromRefreshToken(Client client, Credentials credentials, ValidToken token);
}
