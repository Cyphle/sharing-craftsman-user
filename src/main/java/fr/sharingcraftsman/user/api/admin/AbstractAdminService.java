package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.common.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class AbstractAdminService {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  ClientOrganisation clientOrganisation;
  AuthorizationManager authorizationManager;

  ResponseEntity isUnauthorized(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (isAuthorizedClient(clientDTO, tokenDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);
    return null;
  }

  boolean isAuthorizedClient(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (!clientOrganisation.doesClientExist(ClientDTO.fromApiToDomain(clientDTO))) {
      log.warn("UserEntity " + tokenDTO.getUsername() + " is trying to access restricted admin area with client: " + clientDTO.getName());
      return true;
    }
    return false;
  }

  HttpStatus isAdmin(TokenDTO tokenDTO) {
    try {
      Authorization requesterAuthorization = authorizationManager.getAuthorizationsOf(Username.from(tokenDTO.getUsername()));

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
