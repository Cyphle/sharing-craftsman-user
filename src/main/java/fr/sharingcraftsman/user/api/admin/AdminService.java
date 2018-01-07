package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.GroupDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.pivots.AdminCollaboratorPivot;
import fr.sharingcraftsman.user.api.pivots.AuthorizationPivot;
import fr.sharingcraftsman.user.api.pivots.ClientPivot;
import fr.sharingcraftsman.user.api.pivots.GroupPivot;
import fr.sharingcraftsman.user.domain.admin.UserForAdmin;
import fr.sharingcraftsman.user.domain.admin.ports.UserForAdminRepository;
import fr.sharingcraftsman.user.domain.admin.AdministrationImpl;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authorization.*;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.PasswordException;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.user.exceptions.UserException;
import fr.sharingcraftsman.user.domain.admin.ports.Administration;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

@Service
public class AdminService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private ClientOrganisation clientOrganisation;
  private Administration company;
  private AuthorizationManager authorizationManager;

  @Autowired
  public AdminService(
          UserForAdminRepository userForAdminRepository,
          ClientRepository clientRepository,
          UserAuthorizationRepository userAuthorizationRepository,
          AuthorizationRepository authorizationRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    company = new AdministrationImpl(userForAdminRepository);
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  public ResponseEntity getUsers(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    List<UserForAdmin> collaborators = company.getAllCollaborators();
    List<AdminUserDTO> users = collaborators.stream()
            .map(collaborator -> new AdminUserDTO(
                    collaborator.getUsernameContent(),
                    collaborator.getPassword(),
                    collaborator.getFirstname(),
                    collaborator.getLastname(),
                    collaborator.getEmail(),
                    collaborator.getWebsite(),
                    collaborator.getGithub(),
                    collaborator.getLinkedin(),
                    collaborator.getChangePasswordKey(),
                    collaborator.getChangePasswordKeyExpirationDate(),
                    collaborator.isActive(),
                    collaborator.getCreationDate(),
                    collaborator.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
    users.forEach(user -> {
      try {
        Credentials credentials = Credentials.build(user.getUsername(), "NOPASSWORD");
        Authorization authorization = authorizationManager.getAuthorizationsOf(credentials);
        user.setAuthorizations(AuthorizationPivot.fromDomainToApi(authorization));
      } catch (UsernameException | PasswordException e) {
        e.printStackTrace();
      }
    });

    return ResponseEntity.ok(users);
  }

  public ResponseEntity deleteUser(ClientDTO clientDTO, TokenDTO tokenDTO, String usernameToDelete) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    try {
      company.deleteCollaborator(usernameBuilder.from(usernameToDelete));
      return ResponseEntity.ok().build();
    } catch (UsernameException | UserException e) {
      log.warn("Error while deleting user " + usernameToDelete + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity updateUser(ClientDTO clientDTO, TokenDTO tokenDTO, AdminUserDTO user) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    try {
      UserForAdmin collaborator = AdminCollaboratorPivot.fromApiToDomain(user);
      company.updateCollaborator(collaborator);
      return ResponseEntity.ok().build();
    } catch (UserException e) {
      log.warn("Error while updating user " + user.getUsername() + ": " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity addUser(ClientDTO clientDTO, TokenDTO tokenDTO, AdminUserDTO user) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    try {
      UserForAdmin collaborator = AdminCollaboratorPivot.fromApiToDomain(user);
      company.createCollaborator(collaborator);
      authorizationManager.addGroup(Credentials.build(user.getUsername(), "NOPASSWORD"), Groups.USERS);
      return ResponseEntity.ok().build();
    } catch (UserException | UsernameException | PasswordException e) {
      log.warn("Error:" + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity getGroups(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    Set<GroupDTO> groups = GroupPivot.groupFromDomainToApi(authorizationManager.getAllRolesWithTheirGroups());
    return ResponseEntity.ok(groups);
  }

  public ResponseEntity addGroupToUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserGroupDTO userGroupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    try {
      authorizationManager.addGroup(Credentials.build(userGroupDTO.getUsername(), "NOPASSWORD"), Groups.valueOf(userGroupDTO.getGroup()));
      return ResponseEntity.ok().build();
    } catch (UsernameException | PasswordException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity removeGroupToUser(ClientDTO clientDTO, TokenDTO tokenDTO, UserGroupDTO userGroupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    try {
      authorizationManager.removeGroup(Credentials.build(userGroupDTO.getUsername(), "NOPASSWORD"), Groups.valueOf(userGroupDTO.getGroup()));
      return ResponseEntity.ok().build();
    } catch (UsernameException | PasswordException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
  }

  public ResponseEntity createNewGroupWithRoles(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    authorizationManager.createNewGroupWithRoles(GroupPivot.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }

  public ResponseEntity removeRoleFromGroup(ClientDTO clientDTO, TokenDTO tokenDTO, GroupDTO groupDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);

    authorizationManager.removeRoleFromGroup(GroupPivot.fromApiToDomain(groupDTO));
    return ResponseEntity.ok().build();
  }

  private boolean isAuthorizedClient(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientOrganisation.clientExists(ClientPivot.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to access restricted admin area with client: " + clientDTO.getName());
      return true;
    }
    return false;
  }

  private HttpStatus isAdmin(TokenDTO tokenDTO) {
    try {
      Credentials credentials = Credentials.build(tokenDTO.getUsername(), "NOPASSWORD");
      Authorization requesterAuthorization = authorizationManager.getAuthorizationsOf(credentials);

      Optional<Group> adminGroup = requesterAuthorization.getGroups()
              .stream()
              .filter(group -> group.getName().contains("ADMIN"))
              .findAny();
      if (adminGroup.isPresent()) {
        if (adminGroup.get()
                .getRoles()
                .stream()
                .noneMatch(role -> role.getName().contains("ADMIN"))) {
          return HttpStatus.UNAUTHORIZED;
        }
      } else {
        return HttpStatus.UNAUTHORIZED;
      }
    } catch (CredentialsException e) {
      log.warn("Error with getting authorizations " + tokenDTO.getUsername() + ": " + e.getMessage());
      return HttpStatus.BAD_REQUEST;
    }
    return HttpStatus.OK;
  }
}
