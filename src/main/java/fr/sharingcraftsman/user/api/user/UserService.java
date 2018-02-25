package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.authentication.LoginDTO;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.AuthenticationManagerImpl;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.authorization.UserAuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.AbstractProfile;
import fr.sharingcraftsman.user.domain.user.ChangePasswordToken;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.UserOrganisationImpl;
import fr.sharingcraftsman.user.domain.user.exceptions.InvalidChangePasswordTokenException;
import fr.sharingcraftsman.user.domain.user.exceptions.ProfileValidationException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.ChangePasswordTokenRepository;
import fr.sharingcraftsman.user.domain.user.ports.UserOrganisation;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final AuthenticationManager authenticationManager;
  private UserOrganisation userOrganisation;
  private UserAuthorizationManager userAuthorizationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public UserService(
          UserRepository userRepository,
          AccessTokenRepository accessTokenRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          ChangePasswordTokenRepository changePasswordTokenRepository,
          DateService dateService,
          AuthorizationVerifierService authorizationVerifierService) {
    userOrganisation = new UserOrganisationImpl(userRepository, changePasswordTokenRepository, dateService);
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    userAuthorizationManager = new UserAuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  ResponseEntity getChangePasswordToken(ClientDTO clientDTO, TokenDTO tokenDTO) {
    log.info("[UserService::getChangePasswordToken] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("Request for a change password token for:" + tokenDTO.getUsername());

      if (verifyToken(clientDTO, tokenDTO))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      ChangePasswordToken changePasswordToken = userOrganisation.createChangePasswordTokenFor(Username.from(tokenDTO.getUsername()));
      return ResponseEntity.ok(ChangePasswordTokenDTO.fromDomainToApi(changePasswordToken));
    } catch (UnknownUserException | CredentialsException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity changePassword(ClientDTO clientDTO, TokenDTO tokenDTO, ChangePasswordDTO changePasswordDTO) {
    log.info("[UserService::changePassword] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      if (verifyToken(clientDTO, tokenDTO))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      authenticationManager.logout(Client.from(clientDTO.getName(), ""), Username.from(tokenDTO.getUsername()), TokenDTO.fromApiToDomain(tokenDTO));
      userOrganisation.changePasswordOfUser(Username.from(tokenDTO.getUsername()), ChangePasswordDTO.fromApiToDomain(changePasswordDTO));
      return ResponseEntity.ok().build();
    } catch (CredentialsException | UserException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity registerUser(ClientDTO clientDTO, LoginDTO loginDTO) {
    log.info("[UserService::registerUser] Client: " + clientDTO.getName() + ", User: " + loginDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("UserEntity is registering with username:" + loginDTO.getUsername());

      userOrganisation.createNewUser(LoginDTO.fromApiToDomain(loginDTO));
      userAuthorizationManager.addGroupToUser(Username.from(loginDTO.getUsername()), Groups.USERS);
      return ResponseEntity.ok().build();
    } catch (CredentialsException | UserException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity getLostPasswordToken(ClientDTO clientDTO, String username) {
    log.info("[UserService::getChangePasswordToken] Client: " + clientDTO.getName() + ", User: " + username);

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      ChangePasswordTokenForLostPasswordDTO changePasswordTokenForLostPassword = ChangePasswordTokenForLostPasswordDTO.from(
              userOrganisation.createChangePasswordTokenFor(Username.from(username)),
              userOrganisation.findEmailOf(Username.from(username))
      );
      return ResponseEntity.ok(changePasswordTokenForLostPassword);
    } catch (UserException | CredentialsException e) {
      return logAndSendBadRequest(e);
    }
  }

  public ResponseEntity changeLostPassword(ClientDTO clientDTO, TokenDTO tokenDTO, ChangePasswordDTO changePasswordDTO) {
    log.info("[UserService::changeLostPassword] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      authenticationManager.deleteToken(Client.from(clientDTO.getName(), ""), Username.from(tokenDTO.getUsername()));
      userOrganisation.changeLostPasswordOfUser(Username.from(tokenDTO.getUsername()), ChangePasswordDTO.fromApiToDomainNoOldPassword(changePasswordDTO));
      return ResponseEntity.ok().build();
    } catch (CredentialsException | UserException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity updateProfile(ClientDTO clientDTO, TokenDTO tokenDTO, ProfileDTO profileDTO) {
    log.info("[UserService::updateProfile] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      log.info("Request for a update profile with token:" + tokenDTO.getUsername());

      if (verifyToken(clientDTO, tokenDTO))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      AbstractProfile updatedAbstractProfile = userOrganisation.updateProfile(ProfileDTO.fromApiToDomain(tokenDTO.getUsername(), profileDTO));
      return ResponseEntity.ok(ProfileDTO.fromDomainToApi((Profile) updatedAbstractProfile));
     } catch (ProfileValidationException e) {
      log.warn("Validation errors: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getErrors());
    } catch (CredentialsException | UserException e) {
      return logAndSendBadRequest(e);
    }
  }

  private boolean verifyToken(ClientDTO clientDTO, TokenDTO tokenDTO) throws UsernameException {
    Client client = Client.from(clientDTO.getName(), "");
    return !authenticationManager.isTokenValid(client, Username.from(tokenDTO.getUsername()), TokenDTO.fromApiToDomain(tokenDTO));
  }

  private ResponseEntity logAndSendBadRequest(Exception e) {
    log.warn("Error: " + e.getMessage());
    return ResponseEntity
            .badRequest()
            .body(e.getMessage());
  }
}
