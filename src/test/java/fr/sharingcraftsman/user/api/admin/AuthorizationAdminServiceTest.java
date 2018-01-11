package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import fr.sharingcraftsman.user.domain.authorization.ports.UserAuthorizationRepository;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.common.Username;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationAdminServiceTest {
  @Mock
  private UserAuthorizationRepository userAuthorizationRepository;
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private ClientRepository clientRepository;

  private AuthorizationAdminService authorizationAdminService;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "secret"));

    clientDTO = new ClientDTO("client", "secret");

    tokenDTO = new TokenDTO();
    tokenDTO.setUsername("admin@toto.fr");
    tokenDTO.setAccessToken("aaa");

    authorizationAdminService = new AuthorizationAdminService(clientRepository, userAuthorizationRepository, authorizationRepository);
  }

  @Test
  public void should_get_groups_and_roles() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Group users = Group.from("USERS");
    users.addRole(Role.from("ROLE_USER"));
    Group admins = Group.from("ADMINS");
    admins.addRoles(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<Group> groups = new HashSet<>();
    groups.add(users);
    groups.add(admins);
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(groups);

    ResponseEntity response = authorizationAdminService.getGroups(clientDTO, tokenDTO);

    GroupDTO groupUser = new GroupDTO("USERS");
    groupUser.addRoles(Collections.singletonList(new RoleDTO("ROLE_USER")));
    GroupDTO groupAdmin = new GroupDTO("ADMINS");
    groupAdmin.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    Set<GroupDTO> groupsDTO = new HashSet<>();
    groupsDTO.add(groupUser);
    groupsDTO.add(groupAdmin);
    assertThat(response.getBody()).isEqualTo(groupsDTO);
  }

  @Test
  public void should_create_new_group_with_roles() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(new RoleDTO("ROLE_ROOT"));
    roles.add(new RoleDTO("ROLE_ADMIN"));
    roles.add(new RoleDTO("ROLE_USER"));
    GroupDTO newGroup = new GroupDTO("SUPER_ADMINS", roles);

    ResponseEntity response = authorizationAdminService.createNewGroupWithRoles(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    given(userAuthorizationRepository.findGroupsOf(Username.from("admin@toto.fr"))).willReturn(Collections.singletonList(Group.from("ADMINS")));
    given(authorizationRepository.getRolesOf("ADMINS")).willReturn(Arrays.asList(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")));
    Set<RoleDTO> roles = new HashSet<>();
    roles.add(new RoleDTO("ROLE_ROOT"));
    roles.add(new RoleDTO("ROLE_ADMIN"));
    roles.add(new RoleDTO("ROLE_USER"));
    GroupDTO newGroup = new GroupDTO("SUPER_ADMINS", roles);

    ResponseEntity response = authorizationAdminService.removeRoleFromGroup(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
