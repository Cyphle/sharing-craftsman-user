package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationManagerTest {
  private AuthorizationManager authorizationManager;
  @Mock
  private AuthorizationRepository authorizationRepository;

  @Before
  public void setUp() throws Exception {
    authorizationManager = new AuthorizationManagerImpl(authorizationRepository);
  }

  @Test
  public void should_get_all_groups_and_roles() throws Exception {
    authorizationManager.getAllRolesWithTheirGroups();

    verify(authorizationRepository).getAllRolesWithTheirGroups();
  }

  @Test
  public void should_create_new_group() throws Exception {
    Group groupWithRoles = Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ROOT"), Role.from("ROLE_ADMIN"), Role.from("ROLE_USER"))));

    authorizationManager.createNewGroupWithRoles(groupWithRoles);

    Group groupRoot = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT"))));
    Group groupAdmin = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN"))));
    Group groupUser = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER"))));
    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(groupRoot, groupAdmin, groupUser));
  }

  @Test
  public void should_not_create_group_if_already_exists() throws Exception {
    Group superAdminUser = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER"))));
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(new HashSet<>(Collections.singletonList(superAdminUser)));
    Group groupWithRoles = Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ROOT"), Role.from("ROLE_ADMIN"), Role.from("ROLE_USER"))));

    authorizationManager.createNewGroupWithRoles(groupWithRoles);

    Group groupRoot = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT"))));
    Group groupAdmin = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN"))));
    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(groupRoot, groupAdmin));
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    Group groupWithRoles = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER"))));

    authorizationManager.removeRoleFromGroup(groupWithRoles);

    Group groupUser = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER"))));
    verify(authorizationRepository).removeRoleFromGroup(groupUser);
  }

  @Test
  public void should_remove_first_role_given_from_group() throws Exception {
    Group groupWithRoles = Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ADMIN"), Role.from("ROLE_USER"))));

    authorizationManager.removeRoleFromGroup(groupWithRoles);

    Group groupUser = Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN"))));
    verify(authorizationRepository).removeRoleFromGroup(groupUser);
  }
}
