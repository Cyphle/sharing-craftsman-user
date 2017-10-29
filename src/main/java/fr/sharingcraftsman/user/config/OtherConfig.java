package fr.sharingcraftsman.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
public class OtherConfig {
  @Value("${test.username}")
  private String username;

  @Value("${test.password}")
  private String password;

  public String myBean() {
    return this.username + password;
  }
}
