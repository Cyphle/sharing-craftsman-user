package fr.sharingcraftsman.user.api.admin;

import com.google.common.collect.Sets;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.authorization.GroupDTO;
import fr.sharingcraftsman.user.api.authorization.RoleDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.api.common.AuthorizationVerifierService;
import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import fr.sharingcraftsman.user.domain.authorization.ports.AuthorizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationAdminServiceTest {
  @Mock
  private AuthorizationRepository authorizationRepository;
  @Mock
  private AuthorizationVerifierService authorizationVerifierService;

  private AuthorizationAdminService authorizationAdminService;

  private ClientDTO clientDTO;
  private TokenDTO tokenDTO;

  @Before
  public void setUp() throws Exception {
    given(authorizationVerifierService.isUnauthorizedAdmin(any(ClientDTO.class), any(TokenDTO.class))).willReturn(null);

    clientDTO = ClientDTO.from("client", "secret");
    tokenDTO = TokenDTO.from("admin@toto.fr", "aaa");
    authorizationAdminService = new AuthorizationAdminService(authorizationRepository, authorizationVerifierService);
  }

  @Test
  public void should_get_groups_and_roles() throws Exception {
    Set<Group> groups = Sets.newHashSet(
            Group.from("USERS", Sets.newHashSet(Role.from("ROLE_USER"))),
            Group.from("ADMINS", Sets.newHashSet(Role.from("ROLE_USER"), Role.from("ROLE_ADMIN")))
    );
    given(authorizationRepository.getAllRolesWithTheirGroups()).willReturn(groups);

    ResponseEntity response = authorizationAdminService.getGroups(clientDTO, tokenDTO);

    Set<GroupDTO> groupsDTO = Sets.newHashSet(
            GroupDTO.from("USERS", Sets.newHashSet(RoleDTO.from("ROLE_USER"))),
            GroupDTO.from("ADMINS", Sets.newHashSet(RoleDTO.from("ROLE_USER"), RoleDTO.from("ROLE_ADMIN")))
    );
    assertThat(response.getBody()).isEqualTo(groupsDTO);
  }

  @Test
  public void should_create_new_group_with_roles() throws Exception {
    GroupDTO newGroup = GroupDTO.from("SUPER_ADMINS", Sets.newHashSet(
            RoleDTO.from("ROLE_ROOT"),
            RoleDTO.from("ROLE_ADMIN"),
            RoleDTO.from("ROLE_USER")
    ));

    ResponseEntity response = authorizationAdminService.createNewGroupWithRoles(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_remove_role_from_group() throws Exception {
    GroupDTO newGroup = GroupDTO.from("SUPER_ADMINS", Sets.newHashSet(
            RoleDTO.from("ROLE_ROOT"),
            RoleDTO.from("ROLE_ADMIN"),
            RoleDTO.from("ROLE_USER")
    ));

    ResponseEntity response = authorizationAdminService.removeRoleFromGroup(clientDTO, tokenDTO, newGroup);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
