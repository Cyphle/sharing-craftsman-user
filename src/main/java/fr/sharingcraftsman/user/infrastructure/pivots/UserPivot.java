package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static fr.sharingcraftsman.user.domain.common.Password.passwordBuilder;
import static fr.sharingcraftsman.user.domain.common.Username.usernameBuilder;

public class UserPivot {
  public static User fromDomainToInfra(Collaborator collaborator) {
    return new User(collaborator.getUsername(), collaborator.getPassword());
  }

  public static Collaborator fromInfraToDomain(User user) throws CredentialsException {
    String changePasswordKey = user.getChangePasswordKey() != null ? user.getChangePasswordKey() : "";
    LocalDateTime changePasswordKeyExpirationDate = user.getChangePasswordExpirationDate() != null ? LocalDateTime.ofInstant(user.getChangePasswordExpirationDate().toInstant(), ZoneId.systemDefault()) : null;

    return (new CollaboratorBuilder())
            .withUsername(usernameBuilder.from(user.getUsername()))
            .withPassword(passwordBuilder.from(user.getPassword()))
            .withChangePasswordKey(changePasswordKey)
            .withChangePasswordKeyExpirationDate(changePasswordKeyExpirationDate)
            .build();
  }

  public static KnownProfile fromInfraToDomainProfile(User user) throws UsernameException {
    return new ProfileBuilder()
            .withUsername(usernameBuilder.from(user.getUsername()))
            .withFirstname(Name.of(user.getFirstname()))
            .withLastname(Name.of(user.getLastname()))
            .withEmail(Email.from(user.getEmail()))
            .withWebsite(Link.to(user.getWebsite()))
            .withGithub(Link.to(user.getGithub()))
            .withLinkedin(Link.to(user.getLinkedin()))
            .build();
  }

  public static User fromDomainToInfraProfile(KnownProfile profile) {
    return new User(profile.getUsernameContent(), profile.getFirstnameContent(), profile.getLastnameContent(), profile.getEmailContent(), profile.getWebsiteContent(), profile.getGithubContent(), profile.getLinkedinContent());
  }
}
