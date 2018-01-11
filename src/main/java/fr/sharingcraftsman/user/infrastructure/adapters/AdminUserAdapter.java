package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.admin.ports.AdminUserRepository;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserAdapter implements AdminUserRepository {
  private UserJpaRepository userRepository;

  @Autowired
  public AdminUserAdapter(UserJpaRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public List<User> getAllUsers() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(user -> {
                      try {
                        return User.from(
                                !user.getUsername().isEmpty() ? user.getUsername() : "EMPTYUSERNAME",
                                user.getPassword() != null && !user.getPassword().isEmpty() ? user.getPassword() : "EMPTYPASSWORD");
                      } catch (UsernameException | PasswordException e) {
                        return null;
                      }
                    }
            )
            .collect(Collectors.toList());
  }

  @Override
  public List<Profile> getAllProfiles() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(profile -> {
              try {
                return Profile.from(
                        !profile.getUsername().isEmpty() ? Username.from(profile.getUsername()) : null,
                        Name.of(profile.getFirstname()),
                        Name.of(profile.getLastname()),
                        Email.from(profile.getEmail()),
                        Link.to(profile.getWebsite()),
                        Link.to(profile.getGithub()),
                        Link.to(profile.getLinkedin())
                );
              } catch (UsernameException e) { return null; }
            })
            .collect(Collectors.toList());
  }

  @Override
  public List<TechnicalUserDetails> getAllTechnicalUserDetails() {
    return Lists.newArrayList(userRepository.findAll())
            .stream()
            .map(detail -> {
              try {
                return TechnicalUserDetails.from(
                        !detail.getUsername().isEmpty() ? Username.from(detail.getUsername()) : null,
                        detail.isActive(),
                        DateConverter.fromDateToLocalDateTime(detail.getCreationDate()),
                        DateConverter.fromDateToLocalDateTime(detail.getLastUpdateDate())
                );
              } catch (UsernameException e) { return null; }
            })
            .collect(Collectors.toList());
  }
}
