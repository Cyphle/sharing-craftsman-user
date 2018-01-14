package fr.sharingcraftsman.user.api.file;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class FileController {
  private final StorageService storageService;

  @Autowired
  public FileController(StorageService storageService) {
    this.storageService = storageService;
  }

  @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@RequestHeader("client") String client,
                                            @RequestHeader("secret") String secret,
                                            @RequestHeader("username") String username,
                                            @RequestHeader("access-token") String accessToken,
                                            @PathVariable String filename) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return storageService.loadAsResource(clientDTO, tokenDTO, filename);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity handleFileUpload(@RequestHeader("client") String client,
                                         @RequestHeader("secret") String secret,
                                         @RequestHeader("username") String username,
                                         @RequestHeader("access-token") String accessToken,
                                         @RequestParam("file") MultipartFile file) {
    ClientDTO clientDTO = ClientDTO.from(client, secret);
    TokenDTO tokenDTO = TokenDTO.from(username, accessToken);
    return storageService.store(clientDTO, tokenDTO, file);
  }
}
