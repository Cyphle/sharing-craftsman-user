package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
  public ResponseEntity getUsers(ClientDTO clientDTO, TokenDTO tokenDTO) {
    throw new UnsupportedOperationException();
  }
}
