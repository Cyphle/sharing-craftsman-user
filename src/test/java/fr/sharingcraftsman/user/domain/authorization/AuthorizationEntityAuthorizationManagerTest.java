package fr.sharingcraftsman.user.domain.authorization;

import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationManager;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationEntityAuthorizationManagerTest {
  private AuthorizationManager authorizationManager;
  private Username username;
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;

  @Before
  public void setUp() throws Exception {
    authorizationManager = new AuthorizationManagerImpl(userAuthorizationRepository, authorizationRepository);

    username = Username.from("john@doe.fr");
  }

  @Test
  public void should_get_authorizations_of_user() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));
    given(authorizationRepository.getRolesOf("USERS")).willReturn(Collections.singletonList(Role.from("ROLE_USER")));

    Authorization authorization = authorizationManager.getAuthorizationsOf(username);

    Role role = Role.from("ROLE_USER");
    Group group = Group.from("USERS");
    group.addRole(role);
    Authorization expectedAuthorization = Authorization.get();
    expectedAuthorization.addGroup(group);
    assertThat(authorization).isEqualTo(expectedAuthorization);
  }

  @Test
  public void should_add_given_group_to_user() throws Exception {
    authorizationManager.addGroupToUser(username, Groups.USERS);

    verify(userAuthorizationRepository).addGroupToUser(username, Groups.USERS);
  }

  @Test
  public void should_not_add_group_if_user_already_has_the_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));

    authorizationManager.addGroupToUser(username, Groups.USERS);

    verify(userAuthorizationRepository, never()).addGroupToUser(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_get_all_groups_and_roles() throws Exception {
    authorizationManager.getAllRolesWithTheirGroups();

    verify(authorizationRepository).getAllRolesWithTheirGroups();
  }

  @Test
  public void should_not_remove_group_when_user_does_not_have_it() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("ÆDMINS")));

    authorizationManager.removeGroup(username, Groups.USERS);

    verify(userAuthorizationRepository, never()).removeGroupFromUser(any(Username.class), any(Groups.class));
  }

  @Test
  public void should_remove_group_when_user_has_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(username)).willReturn(Collections.singletonList(Group.from("USERS")));

    authorizationManager.removeGroup(username, Groups.USERS);

    verify(userAuthorizationRepository).removeGroupFromUser(any(Username.class), any(Groups.class));
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
