package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.admin.AdminCollaborator;
import fr.sharingcraftsman.user.domain.admin.HRAdminManager;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.pivots.UserPivot;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HRAdminAdapter implements HRAdminManager {
  private UserRepository userRepository;
  private DateService dateService;

  @Autowired
  public HRAdminAdapter(UserRepository userRepository, DateService dateService) {
    this.userRepository = userRepository;
    this.dateService = dateService;
  }

  @Override
  public List<AdminCollaborator> getAllCollaborators() {
    List<User> users = Lists.newArrayList(userRepository.findAll());
    return UserPivot.fromInfraToAdminDomain(users);
  }
}
