package fr.sharingcraftsman.user.infrastructure.pivots;

import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.common.Email;
import fr.sharingcraftsman.user.domain.common.Link;
import fr.sharingcraftsman.user.domain.common.Name;
import fr.sharingcraftsman.user.domain.common.UsernameException;
import fr.sharingcraftsman.user.domain.company.*;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.domain.authentication.CredentialsException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            .withPassword(user.getPassword() != null ? passwordBuilder.from(user.getPassword()) : null)
            .withChangePasswordKey(changePasswordKey)
            .withChangePasswordKeyExpirationDate(changePasswordKeyExpirationDate)
            .build();
  }

  public static User fromDomainToInfra(AdminCollaborator collaborator) {
    return new User(
            collaborator.getUsernameContent(),
            collaborator.getFirstname(),
            collaborator.getLastname(),
            collaborator.getEmail(),
            collaborator.getWebsite(),
            collaborator.getGithub(),
            collaborator.getLinkedin()
    );
  }

  public static List<AdminCollaborator> fromInfraToAdminDomain(List<User> users) {
    return users.stream()
            .map(user -> AdminCollaborator.from(
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getWebsite(),
                    user.getGithub(),
                    user.getLinkedin(),
                    user.getChangePasswordKey(),
                    user.getChangePasswordExpirationDate(),
                    user.isActive(),
                    user.getCreationDate(),
                    user.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
  }

  public static AdminCollaborator fromInfraToAdminDomain(User user) {
    return AdminCollaborator.from(
            user.getUsername(),
            user.getPassword(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getWebsite(),
            user.getGithub(),
            user.getLinkedin(),
            user.getChangePasswordKey(),
            user.getChangePasswordExpirationDate(),
            user.isActive(),
            user.getCreationDate(),
            user.getLastUpdateDate());
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
