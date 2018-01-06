package fr.sharingcraftsman.user.infrastructure.adapters;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.infrastructure.pivots.GroupPivot;
import fr.sharingcraftsman.user.infrastructure.pivots.RolePivot;
import fr.sharingcraftsman.user.infrastructure.repositories.GroupRoleRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleManagerAdapter implements RoleAdministrator {
  private GroupRoleRepository groupRoleRepository;
  private RoleRepository roleRepository;

  @Autowired
  public RoleManagerAdapter(GroupRoleRepository groupRoleRepository, RoleRepository roleRepository) {
    this.groupRoleRepository = groupRoleRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public List<Role> getRolesOf(String group) {
    return RolePivot.fromInfraToDomain(groupRoleRepository.findByGroup(group));
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return RolePivot.rolesWithGroupfromInfraToDomain(roleRepository.findAll());
  }

  @Override
  public void createNewGroupsWithRole(List<Group> groups) {
    groups.forEach(group -> roleRepository.save(GroupPivot.fromDomainToInfra(group)));
  }
}
