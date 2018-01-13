package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.domain.admin.AdministrationImpl;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.UserAuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdminService {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  private Administration userOrganisation;
  private UserAuthorizationManager userAuthorizationManager;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public UserAdminService(
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          AdminUserRepository adminUserRepository,
          AuthorizationVerifierService authorizationVerifierService) {
    userOrganisation = new AdministrationImpl(adminUserRepository);
    userAuthorizationManager = new UserAuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
    this.authorizationVerifierService = authorizationVerifierService;
  }

  ResponseEntity getAllUsers(ClientDTO clientDTO, TokenDTO tokenDTO) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    List<UserInfoDTO> users = fromUserInfoToUserInfoDTO();
    return ResponseEntity.ok(users);
  }

  ResponseEntity addNewUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserInfoDTO user) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      userOrganisation.createUser(UserInfoDTO.fromApiToDomain(user));
      userAuthorizationManager.addGroupToUser(Username.from(user.getUsername()), Groups.USERS);
      return ResponseEntity.ok().build();
    } catch (UserException | UsernameException | PasswordException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity updateUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserInfoDTO user) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      userOrganisation.updateUser(UserInfoDTO.fromApiToDomain(user));
      return ResponseEntity.ok().build();
    } catch (UserException | PasswordException | UsernameException e) {
      return logAndSendBadRequest(e);
    }
  }

  ResponseEntity deleteUser(ClientDTO clientDTO, TokenDTO tokenDTO, String usernameToDelete) {
    ResponseEntity isUnauthorized = authorizationVerifierService.isUnauthorizedAdmin(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      userOrganisation.deleteUser(Username.from(usernameToDelete));
      return ResponseEntity.ok().build();
    } catch (UsernameException | UserException e) {
      return logAndSendBadRequest(e);
    }
  }

  private List<UserInfoDTO> fromUserInfoToUserInfoDTO() {
    List<UserInfo> fetchedUsers = userOrganisation.getAllUsers();
    List<UserInfoDTO> users = fetchedUsers.stream()
            .map(user -> UserInfoDTO.from(
                    user.getUsernameContent(),
                    user.getPasswordContent(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getWebsite(),
                    user.getGithub(),
                    user.getLinkedin(),
                    user.isActive(),
                    user.getCreationDate(),
                    user.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
    users.forEach(user -> {
      try {
        Authorization authorization = userAuthorizationManager.getAuthorizationsOf(Username.from(user.getUsername()));
        user.setAuthorizations(AuthorizationsDTO.fromDomainToApi(authorization));
      } catch (UsernameException e) {
        e.printStackTrace();
      }
    });
    return users;
  }

  private ResponseEntity logAndSendBadRequest(Exception e) {
    log.warn("Error: " + e.getMessage());
    return ResponseEntity
            .badRequest()
            .body(e.getMessage());
  }
}
