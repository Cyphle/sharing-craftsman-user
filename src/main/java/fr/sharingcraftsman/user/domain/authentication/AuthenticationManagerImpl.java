package fr.sharingcraftsman.user.domain.authentication;

import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.user.BaseUser;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthenticationManagerImpl implements AuthenticationManager {
  private final int LONG_VALIDITY_OFFSET = 8;
  private final int SHORT_VALIDITY_OFFSET = 1;

  private UserRepository userRepository;
  private AccessTokenRepository accessTokenRepository;
  private DateService dateService;

  public AuthenticationManagerImpl(UserRepository userRepository, AccessTokenRepository accessTokenRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.accessTokenRepository = accessTokenRepository;
    this.dateService = dateService;
  }

  @Override
  public BaseToken login(Client client, Credentials credentials) throws UserException {
    BaseUser user = userRepository.findUserFromCredentials(credentials.getEncryptedVersion());
    verifyCollaboratorIsKnown(user);
    accessTokenRepository.deleteTokensOf((User) user, client);
    return generateToken(client, (User) user, credentials.isPersistentLogging());
  }

  @Override
  public boolean isTokenValid(Client client, Username username, AccessToken token) {
    BaseToken foundBaseToken = accessTokenRepository.findTokenFromAccessToken(client, username, token);
    return verifyTokenValidity(foundBaseToken);
  }

  @Override
  public void logout(Client client, Username username, AccessToken token) {
    if (isTokenValid(client, username, token)) {
      deleteToken(username, client);
    }
  }

  @Override
  public boolean isRefreshTokenValid(Client client, Username username, AccessToken token) {
    BaseToken foundBaseToken = accessTokenRepository.findTokenFromRefreshToken(client, username, token);
    return verifyTokenValidity(foundBaseToken);
  }

  @Override
  public void deleteToken(Client client, Username username, AccessToken token) {
    deleteToken(username, client);
  }

  @Override
  public BaseToken createNewToken(Client client, Username username) throws UserException {
    BaseUser baseUser = userRepository.findUserFromUsername(username);
    verifyCollaboratorIsKnown(baseUser);
    User user = (User) baseUser;
    return generateToken(client, user, false);
  }

  private void verifyCollaboratorIsKnown(BaseUser baseUser) throws UnknownUserException {
    if (!baseUser.isKnown())
      throw new UnknownUserException("Unknown collaborator");
  }

  private BaseToken generateToken(Client client, User user, boolean isPersistentLogging) {
    AccessToken token = AccessToken.from(
            generateToken(client.getName() + user.getUsername()),
            generateToken(client.getName() + user.getUsername()),
            dateService.getDayAt(isPersistentLogging ? LONG_VALIDITY_OFFSET : SHORT_VALIDITY_OFFSET)
    );

    return accessTokenRepository.createNewToken(client, user, token);
  }

  private void deleteToken(Username username, Client client) {
    User user = (User) userRepository.findUserFromUsername(username);
    accessTokenRepository.deleteTokensOf(user, client);
  }

  private boolean verifyTokenValidity(BaseToken foundBaseToken) {
    if (foundBaseToken.isValid()) {
      AccessToken validToken = (AccessToken) foundBaseToken;
      if (validToken.getExpirationDate().isBefore(dateService.now()))
        return false;
    }

    return foundBaseToken.isValid();
  }

  private String generateToken(String seed) {
    SecureRandom random = new SecureRandom(seed.getBytes());
    byte bytes[] = new byte[96];
    random.nextBytes(bytes);
    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    return encoder.encodeToString(bytes);
  }
}
