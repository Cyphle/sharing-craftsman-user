package fr.sharingcraftsman.user.api.file;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.config.StorageConfig;
import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Path uploadPath;
  private StorageConfig storageConfig;
  private Environment environment;
  private AuthorizationVerifierService authorizationVerifierService;

  @Autowired
  public FileSystemStorageService(StorageConfig storageConfig, Environment environment, AuthorizationVerifierService authorizationVerifierService) {
    this.storageConfig = storageConfig;
    this.environment = environment;
    this.uploadPath = Paths.get(storageConfig.getLocation());
    this.authorizationVerifierService = authorizationVerifierService;
  }

  @Override
  public void init() {
    try {
      if (!Files.exists(uploadPath))
        Files.createDirectory(uploadPath);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  @Override
  public ResponseEntity store(ClientDTO clientDTO, TokenDTO tokenDTO, MultipartFile file) {
    log.info("[FileSystemStorageService::store] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
      }

      if (!checkFile(file))
        throw new StorageException("File unauthorized");

      String newName = this.generateNewFilename(file);

      if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("prod")))
        Files.copy(file.getInputStream(), uploadPath.resolve(newName));

      return ResponseEntity.ok(newName);
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + file.getOriginalFilename() + ", " + e.getMessage(), e);
    }
  }

  @Override
  public Path load(String filename) {
    return uploadPath.resolve(filename);
  }

  @Override
  public ResponseEntity loadAsResource(ClientDTO clientDTO, TokenDTO tokenDTO, String filename) {
    log.info("[FileSystemStorageService::store] Client: " + clientDTO.getName() + ", User: " + tokenDTO.getUsername());

    if (authorizationVerifierService.isUnauthorizedClient(clientDTO)) return new ResponseEntity("Unknown client", HttpStatus.UNAUTHORIZED);

    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
      } else {
        log.warn("Could not read file: " + filename);
        return ResponseEntity
                .notFound()
                .build();
      }
    } catch (MalformedURLException e) {
      log.warn("Error: " + e.getMessage());
      return ResponseEntity
              .badRequest()
              .body("Could not read file: " + filename + ", " + e.getMessage());
    }
  }

  private String generateNewFilename(MultipartFile file) {
    String[] name = file.getOriginalFilename().split("\\.");
    String extension = name[name.length - 1];
    name = Arrays.copyOf(name, name.length - 1);
    return String.join(".", name) + "_" + UUID.randomUUID() + "." + extension;
  }

  private boolean checkFile(MultipartFile file) {
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    String mimeType = file.getContentType().split("/")[1];

    boolean hasAuthorizedExtension = storageConfig.getAuthorizedExtensions()
            .stream()
            .anyMatch(authorizedExtension -> authorizedExtension.equalsIgnoreCase(extension));
    boolean hasAuthorizedMimeType = storageConfig.getAuthorizedExtensions()
            .stream()
            .anyMatch(authorizedExtension -> authorizedExtension.equalsIgnoreCase(mimeType));

    return hasAuthorizedExtension && hasAuthorizedMimeType;
  }
}
