package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.TokenPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenAdapter implements TokenAdministrator {
  private AccessTokenRepository accessTokenRepository;

  @Autowired
  public TokenAdapter(AccessTokenRepository accessTokenRepository) {
    this.accessTokenRepository = accessTokenRepository;
  }

  @Override
  public void deleteTokensOf(User user, Client client) {
    accessTokenRepository.deleteByUsername(user.getUsername(), client.getName());
  }

  @Override
  public ValidToken createNewToken(Client client, User user, ValidToken token) {
    AccessTokenEntity accessTokenEntity = accessTokenRepository.save(TokenPivot.fromDomainToInfra(user, client, token));
    return TokenPivot.fromInfraToDomain(accessTokenEntity);
  }

  @Override
  public Token findTokenFromAccessToken(Client client, Credentials credentials, ValidToken token) {
    AccessTokenEntity foundToken = accessTokenRepository.findByUsernameClientAndAccessToken(credentials.getUsernameContent(), client.getName(), token.getAccessToken());

    if (foundToken == null)
      return new InvalidToken();

    return TokenPivot.fromInfraToDomain(foundToken);
  }

  @Override
  public Token findTokenFromRefreshToken(Client client, Credentials credentials, ValidToken token) {
    AccessTokenEntity foundToken = accessTokenRepository.findByUsernameClientAndRefreshToken(credentials.getUsernameContent(), client.getName(), token.getRefreshToken());

    if (foundToken == null)
      return new InvalidToken();

    return TokenPivot.fromInfraToDomain(foundToken);
  }
}
