package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authentication.TokenAdministrator;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.Collaborator;
import fr.sharingcraftsman.user.infrastructure.pivots.TokenPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;

public class TokenAdapter implements TokenAdministrator {
  private final int LONG_VALIDITY_OFFSET = 8;
  private final int SHORT_VALIDITY_OFFSET = 1;
  private TokenRepository tokenRepository;
  private DateService dateService;

  @Autowired
  public TokenAdapter(TokenRepository tokenRepository, DateService dateService) {
    this.tokenRepository = tokenRepository;
    this.dateService = dateService;
  }

  @Override
  public void deleteTokensOf(Collaborator collaborator, Client client) {
    tokenRepository.deleteByUsername(collaborator.getUsername(), client.getName());
  }

  @Override
  public ValidToken createNewToken(Collaborator collaborator, Client client, boolean isLongToken) {
    LocalDateTime expirationDate = dateService.getDayAt(isLongToken ? LONG_VALIDITY_OFFSET : SHORT_VALIDITY_OFFSET);

    ValidToken token = validTokenBuilder
            .withAccessToken(generateKey(client.getName() + collaborator.getUsername()))
            .withRefreshToken(generateKey(client.getName() + collaborator.getUsername()))
            .expiringThe(expirationDate)
            .build();

    tokenRepository.save(TokenPivot.fromDomainToInfra(collaborator, client, token));

    return token;
  }

  private String generateKey(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
