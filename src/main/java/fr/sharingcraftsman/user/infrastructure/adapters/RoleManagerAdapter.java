package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.RoleAdministrator;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
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

  @Autowired
  public RoleManagerAdapter(GroupRoleRepository groupRoleRepository) {
    this.groupRoleRepository = groupRoleRepository;
  }

  @Override
  public List<Role> getRolesOf(String group) {
    return RolePivot.fromInfraToDomain(groupRoleRepository.findByGroup(group));
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return RolePivot.fromInfraToDomainRolesGroupedByGroup(groupRoleRepository.findAll());
  }

  @Override
  public void createNewGroupsWithRole(List<Group> groups) {
    groups.forEach(group -> groupRoleRepository.save(GroupPivot.fromDomainToInfra(group)));
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    GroupRole groupRole = groupRoleRepository.findFromGroupNameAndRole(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getRole());
    if (groupRole != null)
      groupRoleRepository.delete(groupRole);
  }
}
