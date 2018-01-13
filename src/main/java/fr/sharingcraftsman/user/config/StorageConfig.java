package fr.sharingcraftsman.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class StorageConfig {
  @Value("${upload.path}")
  private String uploadPath;

  @Value("${upload.extensions.authorized}")
  private String authorizedExtensions;

  public String getLocation() {
    return this.uploadPath;
  }

  public List<String> getAuthorizedExtensions() {
    return Arrays.asList(authorizedExtensions.split(","));
  }
}