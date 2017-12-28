package fr.sharingcraftsman.user.domain.company;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import fr.sharingcraftsman.user.domain.common.Password;
import org.junit.Test;

import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class CollaboratorTest {
  @Test
  public void should_create_user_from_valid_credentials() throws Exception {
    Collaborator collaborator = Collaborator.from(Credentials.buildEncryptedCredentials(usernameBuilder.from("john@doe.fr"), Password.passwordBuilder.from("password"), false));

    assertThat(collaborator).isNotNull();
  }
}
