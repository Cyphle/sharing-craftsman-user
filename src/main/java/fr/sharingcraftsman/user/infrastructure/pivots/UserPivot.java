package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;
import fr.sharingcraftsman.user.domain.company.Collaborator;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;
import static fr.sharingcraftsman.user.domain.company.Collaborator.collaboratorBuilder;

public class UserPivot {
  public static User fromDomainToInfra(Collaborator collaborator) {
    return new User(collaborator.getUsername(), collaborator.getPassword());
  }

  public static Collaborator fromInfraToDomain(User user) throws CredentialsException {
    Collaborator collaborator = collaboratorBuilder
            .withUsername(usernameBuilder.from(user.getUsername()))
            .withPassword(passwordBuilder.from(user.getPassword()))
            .build();
    return collaborator;
  }
}
