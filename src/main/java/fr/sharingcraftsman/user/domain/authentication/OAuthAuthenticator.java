package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;

import java.security.SecureRandom;
import java.util.Base64;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;

public class OAuthAuthenticator implements Authenticator {
  private final int LONG_VALIDITY_OFFSET = 8;
  private final int SHORT_VALIDITY_OFFSET = 1;

  private HumanResourceAdministrator humanResourceAdministrator;
  private TokenAdministrator tokenAdministrator;
  private DateService dateService;

  public OAuthAuthenticator(HumanResourceAdministrator humanResourceAdministrator, TokenAdministrator tokenAdministrator, DateService dateService) {
    this.humanResourceAdministrator = humanResourceAdministrator;
    this.tokenAdministrator = tokenAdministrator;
    this.dateService = dateService;
  }

  @Override
  public Token login(Credentials credentials, Client client) throws CollaboratorException {
    Person person = humanResourceAdministrator.findFromCredentials(credentials);

    if (!person.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");

    Collaborator collaborator = (Collaborator) person;
    tokenAdministrator.deleteTokensOf(collaborator, client);

    ValidToken token = validTokenBuilder
            .withAccessToken(generateKey(client.getName() + collaborator.getUsername()))
            .withRefreshToken(generateKey(client.getName() + collaborator.getUsername()))
            .expiringThe(dateService.getDayAt(credentials.stayLogged() ? LONG_VALIDITY_OFFSET : SHORT_VALIDITY_OFFSET))
            .build();

    return tokenAdministrator.createNewToken(client, collaborator, token);
  }

  @Override
  public boolean isTokenValid(Credentials credentials, Client client, ValidToken token) {
    Token foundToken = tokenAdministrator.findTokenFromAccessToken(client, credentials, token);

    if (foundToken.isValid()) {
      ValidToken validToken = (ValidToken) foundToken;
      if (validToken.getExpirationDate().isBefore(dateService.now()))
        return false;
    }

    return foundToken.isValid();
  }

  @Override
  public void logout(Credentials credentials, Client client, ValidToken token) {
    if (isTokenValid(credentials, client, token)) {
      Collaborator collaborator = (Collaborator) humanResourceAdministrator.findFromCredentials(credentials);
      tokenAdministrator.deleteTokensOf(collaborator, client);
    }
  }

  @Override
  public boolean isRefreshTokenValid(Credentials credentials, Client client, ValidToken token) {
    Token foundToken = tokenAdministrator.findTokenFromRefreshToken(client, credentials, token);

    return foundToken.isValid();
  }

  private String generateKey(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
