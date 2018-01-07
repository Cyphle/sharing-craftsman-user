package fr.sharingcraftsman.user.api.user;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.api.pivots.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authentication.AuthenticationManagerImpl;
import fr.sharingcraftsman.user.domain.authentication.ports.AccessTokenRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.*;
import fr.sharingcraftsman.user.domain.authentication.ports.AuthenticationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.user.exceptions.ProfileValidationException;
import fr.sharingcraftsman.user.domain.user.exceptions.UnknownUserException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.user.ports.UserOrganisation;
import fr.sharingcraftsman.user.domain.user.ports.UserRepository;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
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
  private ClientOrganisation clientOrganisation;
  private AuthorizationManager authorizationManager;

  @Autowired
  public UserService(
          UserRepository userRepository,
          ClientRepository clientRepository,
          AccessTokenRepository accessTokenRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          DateService dateService) {
    userOrganisation = new UserOrganisationImpl(userRepository, dateService);
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    authenticationManager = new AuthenticationManagerImpl(userRepository, accessTokenRepository, dateService);
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  public ResponseEntity registerUser(ClientDTO clientDTO, LoginDTO loginDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + loginDTO.getUsername() + " is trying to log in with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("UserEntity is registering with username:" + loginDTO.getUsername());
      Credentials credentials = LoginPivot.fromApiToDomain(loginDTO);

      userOrganisation.createNewCollaborator(credentials);
      authorizationManager.addGroup(credentials, Groups.USERS);
      return ResponseEntity.ok().build();
    } catch (CredentialsException | UserException e) {
      log.warn("Error with registering " + loginDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity requestChangePassword(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to request for change password with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Request for a change password token for:" + tokenDTO.getUsername());
      Credentials credentials = Credentials.build(tokenDTO.getUsername(), "NOPASSWORD");

      if (verifyToken(clientDTO, tokenDTO, credentials))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      ChangePasswordKey changePasswordKey = userOrganisation.createChangePasswordKeyFor(credentials);
      return ResponseEntity.ok(ChangePasswordTokenPivot.fromDomainToApi(changePasswordKey));
    } catch (UsernameException | PasswordException | UnknownUserException e) {
      log.warn("Error with change password request " + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity changePassword(ClientDTO clientDTO, TokenDTO tokenDTO, ChangePasswordDTO changePasswordDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to change password with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Request for a change password token for:" + tokenDTO.getUsername());
      Credentials credentials = Credentials.buildWithEncryption(tokenDTO.getUsername(), changePasswordDTO.getOldPassword());

      if (verifyToken(clientDTO, tokenDTO, credentials))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      authenticationManager.logout(credentials, new Client(clientDTO.getName(), "", false), TokenPivot.fromApiToDomain(tokenDTO));
      userOrganisation.changePassword(credentials, ChangePasswordPivot.fromApiToDomain(changePasswordDTO));
      return ResponseEntity.ok().build();
    } catch (CredentialsException | UserException e) {
      log.warn("Error with change password: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity updateProfile(ClientDTO clientDTO, TokenDTO tokenDTO, ProfileDTO profileDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to change profile with unauthorized client: " + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      log.info("Request for a update profile with token:" + tokenDTO.getUsername());
      Credentials credentials = Credentials.build(tokenDTO.getUsername(), "NOPASSWORD");

      if (verifyToken(clientDTO, tokenDTO, credentials))
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);

      BaseProfile updatedBaseProfile = userOrganisation.updateProfile(ProfilePivot.fromApiToDomain(tokenDTO.getUsername(), profileDTO));
      return ResponseEntity.ok(ProfilePivot.fromDomainToApi((Profile) updatedBaseProfile));
     } catch (ProfileValidationException e) {
      log.warn("Validation errors with update profile:" + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getErrors());
    } catch (CredentialsException | UserException e) {
      log.warn("Error with update profile " + tokenDTO.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity generateLostPasswordKey(ClientDTO clientDTO, String username) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("Un authorized client:" + clientDTO.getName());
      return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);
    }

    try {
      Credentials credentials = Credentials.build(username, "NOPASSWORD");
      ChangePasswordKey changePasswordKey = userOrganisation.createChangePasswordKeyFor(credentials);
      Email email = userOrganisation.findEmailOf(credentials);
      ChangePasswordKeyForLostPasswordDTO changePasswordKeyForLostPassword = new ChangePasswordKeyForLostPasswordDTO(changePasswordKey, email);
      return ResponseEntity.ok(changePasswordKeyForLostPassword);
    } catch (UsernameException | PasswordException | UserException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  private boolean verifyToken(ClientDTO clientDTO, TokenDTO tokenDTO, Credentials credentials) {
    Client client = new Client(clientDTO.getName(), "", false);
    return !authenticationManager.isTokenValid(credentials, client, TokenPivot.fromApiToDomain(tokenDTO));
  }
}
