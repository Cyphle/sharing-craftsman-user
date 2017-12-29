package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.*;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.infrastructure.models.OAuthToken;
import fr.sharingcraftsman.user.infrastructure.pivots.TokenPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenAdapter implements TokenAdministrator {
  private TokenRepository tokenRepository;

  @Autowired
  public TokenAdapter(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @Override
  public void deleteTokensOf(Collaborator collaborator, Client client) {
    tokenRepository.deleteByUsername(collaborator.getUsername(), client.getName());
  }

  @Override
  public ValidToken createNewToken(Client client, Collaborator collaborator, ValidToken token) {
    OAuthToken oAuthToken = tokenRepository.save(TokenPivot.fromDomainToInfra(collaborator, client, token));
    return TokenPivot.fromInfraToDomain(oAuthToken);
  }

  @Override
  public Token findTokenFromAccessToken(Client client, Credentials credentials, ValidToken token) {
    OAuthToken foundToken = tokenRepository.findByUsernameClientAndAccessToken(credentials.getUsernameContent(), client.getName(), token.getAccessToken());

    if (foundToken == null)
      return new InvalidToken();

    return TokenPivot.fromInfraToDomain(foundToken);
  }

  @Override
  public Token findTokenFromRefreshToken(Client client, Credentials credentials, ValidToken token) {
    throw new UnsupportedOperationException();
  }
}
