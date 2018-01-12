package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.AuthorizationsDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.admin.AdministrationImpl;
import fr.sharingcraftsman.user.domain.admin.UserInfo;
import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.AuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Groups;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdminService extends AbstractAdminService {
  private Administration userOrganisation;

  @Autowired
  public UserAdminService(
          ClientRepository clientRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository,
          AdminUserRepository adminUserRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    userOrganisation = new AdministrationImpl(adminUserRepository);
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  ResponseEntity getAllUsers(ClientDTO clientDTO, TokenDTO tokenDTO) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

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
        Authorization authorization = authorizationManager.getAuthorizationsOf(Username.from(user.getUsername()));
        user.setAuthorizations(AuthorizationsDTO.fromDomainToApi(authorization));
      } catch (UsernameException e) {
        e.printStackTrace();
      }
    });

    return ResponseEntity.ok(users);
  }

  ResponseEntity addUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserInfoDTO user) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      UserInfo userInfo = UserInfoDTO.fromApiToDomain(user);
      userOrganisation.createUser(userInfo);
      authorizationManager.addGroup(Username.from(user.getUsername()), Groups.USERS);
      return ResponseEntity.ok().build();
    } catch (UserException | UsernameException | PasswordException e) {
      log.warn("Error:" + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  ResponseEntity updateUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserInfoDTO user) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      UserInfo userInfo = UserInfoDTO.fromApiToDomain(user);
      userOrganisation.updateUser(userInfo);
      return ResponseEntity.ok().build();
    } catch (UserException | PasswordException | UsernameException e) {
      log.warn("Error while updating user " + user.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  ResponseEntity deleteUser(ClientDTO clientDTO, TokenDTO tokenDTO, String usernameToDelete) {
    ResponseEntity isUnauthorized = isUnauthorized(clientDTO, tokenDTO);
    if (isUnauthorized != null) return isUnauthorized;

    try {
      userOrganisation.deleteUser(Username.from(usernameToDelete));
      return ResponseEntity.ok().build();
    } catch (UsernameException | UserException e) {
      log.warn("Error while deleting user " + usernameToDelete + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }
}
