package fr.sharingcraftsman.acceptance.config;

import fr.sharingcraftsman.user.UserApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(
        classes = {UserApplication.class},
        loader = SpringBootContextLoader.class)
@WebAppConfiguration
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class SpringAcceptanceTestConfig {
  private final String BASE_URI = "http://localhost";
  private final int PORT = 8080;

  protected String getBaseUri() {
    return BASE_URI + ":" + PORT;
  }
}
