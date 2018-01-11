package fr.sharingcraftsman.user.domain.admin.ports;

import fr.sharingcraftsman.user.domain.admin.TechnicalUserDetails;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;

import java.util.List;

public interface AdminUserRepository {
  List<User> getAllUsers();

  List<Profile> getAllProfiles();

  List<TechnicalUserDetails> getAllTechnicalUserDetails();
}
