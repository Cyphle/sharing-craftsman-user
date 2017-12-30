package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  public ResponseEntity getAuthorizations(ClientDTO clientDTO, TokenDTO tokenDTO) {
    return null;
  }
}
