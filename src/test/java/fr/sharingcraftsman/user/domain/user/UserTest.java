package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
  @Test
  public void should_create_user_from_valid_credentials() throws Exception {
    User user = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));

    assertThat(user).isNotNull();
  }
}
