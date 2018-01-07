package fr.sharingcraftsman.user.domain.user;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import org.junit.Test;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
  @Test
  public void should_create_user_from_valid_credentials() throws Exception {
    User user = User.from(Credentials.buildWithEncryption("john@doe.fr", "password"));

    assertThat(user).isNotNull();
  }
}
