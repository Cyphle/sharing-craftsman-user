package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import fr.sharingcraftsman.user.domain.company.Organisation;
import fr.sharingcraftsman.user.domain.ports.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
  private Company company;

  @Autowired
  public AdminService(
          HumanResourceAdministrator humanResourceAdministrator,
          DateService dateService) {
    company = new Organisation(humanResourceAdministrator, dateService);
  }

  public ResponseEntity getUsers(ClientDTO clientDTO, TokenDTO tokenDTO) {
    /*
      - check if token is admin
      - get users
      - get profiles
      - get authorizations
     */
    List<AdminCollaborator> collaborators = company.getAllCollaborators();

    throw new UnsupportedOperationException();
  }
}
