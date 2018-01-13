package fr.sharingcraftsman.user.domain.authorization;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorizationManagerImpl implements AuthorizationManager {
  private AuthorizationRepository authorizationRepository;

  public AuthorizationManagerImpl(AuthorizationRepository authorizationRepository) {
    this.authorizationRepository = authorizationRepository;
  }

  @Override
  public Set<Group> getAllRolesWithTheirGroups() {
    return authorizationRepository.getAllRolesWithTheirGroups();
  }

  @Override
  public void createNewGroupWithRoles(Group group) {
    Group foundGroup = authorizationRepository.getAllRolesWithTheirGroups()
            .stream()
            .filter(fetchedGroup -> fetchedGroup.getName().equals(group.getName()))
            .findFirst()
            .orElse(Group.from(""));

    List<Group> rolesToAdd = group.asSeparatedGroupByRole()
            .stream()
            .filter(role -> !foundGroup.getRoles().contains(Lists.newArrayList(role.getRoles()).get(0)))
            .collect(Collectors.toList());

    authorizationRepository.createNewGroupsWithRole(rolesToAdd);
  }

  @Override
  public void removeRoleFromGroup(Group group) {
    Group filteredGroup = Group.from(group.getName(), new HashSet<>(Collections.singletonList(Lists.newArrayList(group.getRoles()).get(0))));
    authorizationRepository.removeRoleFromGroup(filteredGroup);
  }
}
