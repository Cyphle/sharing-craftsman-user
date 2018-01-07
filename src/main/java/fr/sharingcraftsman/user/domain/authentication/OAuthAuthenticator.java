package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.ports.authentication.Authenticator;
import fr.sharingcraftsman.user.domain.user.ports.HumanResourceAdministrator;

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
    BaseUser baseUser = humanResourceAdministrator.findCollaboratorFromCredentials(credentials);
    verifyCollaboratorIsKnown(baseUser);
    User user = (User) baseUser;
    tokenAdministrator.deleteTokensOf(user, client);
    return generateToken(credentials, client, user);
  }

  @Override
  public boolean isTokenValid(Credentials credentials, Client client, ValidToken token) {
    Token foundToken = tokenAdministrator.findTokenFromAccessToken(client, credentials, token);
    return verifyTokenValidity(foundToken);
  }

  @Override
  public void logout(Credentials credentials, Client client, ValidToken token) {
    if (isTokenValid(credentials, client, token)) {
      deleteToken(credentials, client);
    }
  }

  @Override
  public boolean isRefreshTokenValid(Credentials credentials, Client client, ValidToken token) {
    Token foundToken = tokenAdministrator.findTokenFromRefreshToken(client, credentials, token);
    return verifyTokenValidity(foundToken);
  }

  @Override
  public void deleteToken(Credentials credentials, Client client, ValidToken token) {
    deleteToken(credentials, client);
  }

  @Override
  public Token createNewToken(Credentials credentials, Client client) throws CollaboratorException {
    BaseUser baseUser = humanResourceAdministrator.findCollaboratorFromUsername(credentials.getUsername());
    verifyCollaboratorIsKnown(baseUser);
    User user = (User) baseUser;
    return generateToken(credentials, client, user);
  }

  private void verifyCollaboratorIsKnown(BaseUser baseUser) throws UnknownCollaboratorException {
    if (!baseUser.isKnown())
      throw new UnknownCollaboratorException("Unknown collaborator");
  }

  private Token generateToken(Credentials credentials, Client client, User user) {
    ValidToken token = validTokenBuilder
            .withAccessToken(generateKey(client.getName() + user.getUsername()))
            .withRefreshToken(generateKey(client.getName() + user.getUsername()))
            .expiringThe(dateService.getDayAt(credentials.stayLogged() ? LONG_VALIDITY_OFFSET : SHORT_VALIDITY_OFFSET))
            .build();

    return tokenAdministrator.createNewToken(client, user, token);
  }

  private void deleteToken(Credentials credentials, Client client) {
    User user = (User) humanResourceAdministrator.findCollaboratorFromUsername(credentials.getUsername());
    tokenAdministrator.deleteTokensOf(user, client);
  }

  private boolean verifyTokenValidity(Token foundToken) {
    if (foundToken.isValid()) {
      ValidToken validToken = (ValidToken) foundToken;
      if (validToken.getExpirationDate().isBefore(dateService.now()))
        return false;
    }

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
