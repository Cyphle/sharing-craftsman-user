package fr.sharingcraftsman.user.infrastructure.adapters;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.AuthorizationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleManagerAdapter implements AuthorizationRepository {
  private AuthorizationJpaRepository authorizationJpaRepository;

  @Autowired
  public RoleManagerAdapter(AuthorizationJpaRepository authorizationJpaRepository) {
    this.authorizationJpaRepository = authorizationJpaRepository;
  }

  @Override
  public List<Role> getRolesOf(String group) {
    return AuthorizationEntity.fromInfraToDomain(authorizationJpaRepository.findByGroup(group));
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return AuthorizationEntity.fromInfraToDomainRolesGroupedByGroup(authorizationJpaRepository.findAll());
  }

  @Override
  public void createNewGroupsWithRole(List<Group> groups) {
    groups.forEach(group -> authorizationJpaRepository.save(AuthorizationEntity.fromDomainToInfra(group)));
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    AuthorizationEntity authorizationEntity = authorizationJpaRepository.findFromGroupNameAndRole(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getName());
    if (authorizationEntity != null)
      authorizationJpaRepository.delete(authorizationEntity);
  }
}
