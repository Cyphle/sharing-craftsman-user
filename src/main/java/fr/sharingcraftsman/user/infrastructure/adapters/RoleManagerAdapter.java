package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.infrastructure.pivots.RolePivot;
import fr.sharingcraftsman.user.infrastructure.repositories.GroupRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManagerAdapter implements RoleAdministrator {
  private GroupRoleRepository groupRoleRepository;

  @Autowired
  public RoleManagerAdapter(GroupRoleRepository groupRoleRepository) {
    this.groupRoleRepository = groupRoleRepository;
  }

  @Override
  public List<Role> getRolesOf(String group) {
    return RolePivot.fromInfraToDomain(groupRoleRepository.findByGroup(group));
  }
}
