package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManagerAdapter implements RoleAdministrator {
  @Override
  public List<Role> getRolesOf(String group) {
    throw new UnsupportedOperationException();
  }
}
