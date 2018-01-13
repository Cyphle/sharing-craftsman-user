package fr.sharingcraftsman.user.api.file;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
  void init();
  ResponseEntity store(ClientDTO clientDTO, TokenDTO tokenDTO, MultipartFile file);
  Path load(String filename);
  ResponseEntity loadAsResource(ClientDTO clientDTO, TokenDTO tokenDTO, String filename);
}
