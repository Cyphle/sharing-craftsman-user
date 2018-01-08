package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.AccessTokenEntity;
import fr.sharingcraftsman.user.infrastructure.pivots.TokenPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.AccessTokenJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenAdapter implements AccessTokenRepository {
  private AccessTokenJpaRepository accessTokenJpaRepository;

  @Autowired
  public AccessTokenAdapter(AccessTokenJpaRepository accessTokenJpaRepository) {
    this.accessTokenJpaRepository = accessTokenJpaRepository;
  }

  @Override
  public void deleteTokensOf(User user, Client client) {
    accessTokenJpaRepository.deleteByUsername(user.getUsername(), client.getName());
  }

  @Override
  public AccessToken createNewToken(Client client, User user, AccessToken token) {
    AccessTokenEntity accessTokenEntity = accessTokenJpaRepository.save(TokenPivot.fromDomainToInfra(user, client, token));
    return TokenPivot.fromInfraToDomain(accessTokenEntity);
  }

  @Override
  public BaseToken findTokenFromAccessToken(Client client, Username username, AccessToken token) {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndAccessToken(username.getUsername(), client.getName(), token.getAccessToken());

    if (foundToken == null)
      return new InvalidToken();

    return TokenPivot.fromInfraToDomain(foundToken);
  }

  @Override
  public BaseToken findTokenFromRefreshToken(Client client, Username username, AccessToken token) {
    AccessTokenEntity foundToken = accessTokenJpaRepository.findByUsernameClientAndRefreshToken(username.getUsername(), client.getName(), token.getRefreshToken());

    if (foundToken == null)
      return new InvalidToken();

    return TokenPivot.fromInfraToDomain(foundToken);
  }
}
