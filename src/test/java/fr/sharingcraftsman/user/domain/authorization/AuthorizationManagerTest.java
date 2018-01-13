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
    authorizationManager.createNewGroupWithRoles(Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ROOT"), Role.from("ROLE_ADMIN"), Role.from("ROLE_USER")))));

    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(
            Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT")))),
            Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN")))),
            Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER"))))
    ));
  }

  @Test
  public void should_not_create_group_if_already_exists() throws Exception {
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(new HashSet<>(Collections.singletonList(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER")))))));

    authorizationManager.createNewGroupWithRoles(Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ROOT"), Role.from("ROLE_ADMIN"), Role.from("ROLE_USER")))));

    verify(authorizationRepository).createNewGroupsWithRole(Arrays.asList(
            Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ROOT")))),
            Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN"))))
    ));
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    authorizationManager.removeRoleFromGroup(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER")))));

    verify(authorizationRepository).removeRoleFromGroup(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_USER")))));
  }

  @Test
  public void should_remove_first_role_given_from_group() throws Exception {
    authorizationManager.removeRoleFromGroup(Group.from("SUPER_ADMIN", new HashSet<>(Arrays.asList(Role.from("ROLE_ADMIN"), Role.from("ROLE_USER")))));

    verify(authorizationRepository).removeRoleFromGroup(Group.from("SUPER_ADMIN", new HashSet<>(Collections.singletonList(Role.from("ROLE_ADMIN")))));
  }
}
