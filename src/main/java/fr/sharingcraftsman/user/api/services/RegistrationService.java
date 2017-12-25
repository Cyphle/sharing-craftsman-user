package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.pivots.LoginPivot;
import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.company.CollaboratorException;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Organisation;
import fr.sharingcraftsman.user.domain.ports.company.Company;
import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import fr.sharingcraftsman.user.infrastructure.adapters.UserAdapter;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
  private Company company;

  @Autowired
  public RegistrationService(UserRepository userRepository, DateService dateService) {
    HumanResourceAdministrator humanResourceAdministrator = new UserAdapter(userRepository, dateService);
    company = new Organisation(humanResourceAdministrator);
  }

  public ResponseEntity registerUser(Login login) {
    // TODO need to check client
//    if (!clientManager.clientExists(Client client = ClientPivot.fromApiToDomain(login))) {
//      return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }

    try {
      Credentials credentials = LoginPivot.fromApiToDomain(login);
      company.createNewCollaborator(credentials);
    } catch (CredentialsException | CollaboratorException e) {
      return ResponseEntity
              .badRequest()
              .body(e.getMessage());
    }
    return ResponseEntity.ok().build();
  }
}
