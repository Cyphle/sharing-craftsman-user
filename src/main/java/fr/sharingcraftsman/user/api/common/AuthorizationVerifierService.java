package fr.sharingcraftsman.user.api.common;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.authorization.Authorization;
import fr.sharingcraftsman.user.domain.authorization.UserAuthorizationManagerImpl;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.ClientOrganisationImpl;
import fr.sharingcraftsman.user.domain.client.ports.ClientOrganisation;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import fr.sharingcraftsman.user.domain.utils.SimpleSecretGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationVerifierService {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  private ClientOrganisation clientOrganisation;
  private UserAuthorizationManager userAuthorizationManager;

  @Autowired
  public AuthorizationVerifierService(ClientRepository clientRepository,
                                      UserAuthorizationRepository userAuthorizationRepository,
                                      AuthorizationRepository authorizationRepository) {
    clientOrganisation = new ClientOrganisationImpl(clientRepository, new SimpleSecretGenerator());
    userAuthorizationManager = new UserAuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);
  }

  public ResponseEntity isUnauthorizedAdmin(ClientDTO clientDTO, TokenDTO tokenDTO) {
    if (isUnauthorizedClient(clientDTO)) return new ResponseEntity<>("Unknown client", HttpStatus.UNAUTHORIZED);

    HttpStatus isAdmin = isAdmin(tokenDTO);
    if (!isAdmin.equals(HttpStatus.OK)) return new ResponseEntity<>("Unauthorized user", isAdmin);
    return null;
  }

  public boolean isUnauthorizedClient(ClientDTO clientDTO) {
    if (clientOrganisation.doesClientExist(ClientDTO.fromApiToDomain(clientDTO))) {
      return false;
    }
    log.warn("Unauthorized client: " + clientDTO.getName());
    return true;
  }

  private HttpStatus isAdmin(TokenDTO tokenDTO) {
    try {
      Authorization requesterAuthorization = userAuthorizationManager.getAuthorizationsOf(Username.from(tokenDTO.getUsername()));

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
