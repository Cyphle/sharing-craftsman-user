package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.*;
import fr.sharingcraftsman.user.common.DateService;
import fr.sharingcraftsman.user.domain.authentication.ValidToken;
import fr.sharingcraftsman.user.domain.company.HumanResourceAdministrator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static fr.sharingcraftsman.user.domain.authentication.ValidToken.validTokenBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {
  @Mock
  private HumanResourceAdministrator humanResourceAdministrator;
  @Mock
  private DateService dateService;

  private AdminService adminService;

  private ClientDTO clientDTO;
  private ValidToken validToken;
  private TokenDTO tokenDTO;
  private AdminUserDTO user;
  private AdminUserDTO adminUser;

  @Before
  public void setUp() throws Exception {
    given(dateService.getDayAt(any(Integer.class))).willReturn(LocalDateTime.of(2017, Month.DECEMBER, 30, 12, 0));

    GroupDTO group = new GroupDTO("USERS");
    group.addRole(new RoleDTO("ROLE_USER"));
    AuthorizationsDTO authorization = new AuthorizationsDTO();
    authorization.addGroup(group);
    user = new AdminUserDTO("john@doe.fr", "John", "Doe", "john@doe.fr", "www.johndoe.fr", "github.com/johndoe", "linkedin.com/johndoe", authorization, true, 1514631600000L, 1514631600000L);

    GroupDTO adminGroup = new GroupDTO("ADMINS");
    adminGroup.addRoles(Arrays.asList(new RoleDTO("ROLE_USER"), new RoleDTO("ROLE_ADMIN")));
    AuthorizationsDTO adminAuthorization = new AuthorizationsDTO();
    adminAuthorization.addGroup(adminGroup);
    adminUser = new AdminUserDTO("admin@toto.fr", "Admin", "Toto", "admin@toto.fr", "www.admintoto.fr", "github.com/admintoto", "linkedin.com/admintoto", adminAuthorization, true, 1514631600000L, 1514631600000L);

    clientDTO = new ClientDTO("client", "secret");
    validToken = validTokenBuilder
            .withAccessToken("aaa")
            .withRefreshToken("bbb")
            .expiringThe(dateService.getDayAt(8))
            .build();

    tokenDTO = new TokenDTO();
    tokenDTO.setUsername("admin@toto.fr");
    tokenDTO.setAccessToken("aaa");

    adminService = new AdminService(humanResourceAdministrator, dateService);
  }

  @Test
  public void should_get_list_of_users_with_profile_and_authorizations() throws Exception {
//    given(humanResourceAdministrator.getAllCollaborators()).willReturn(collaborators);

    ResponseEntity users = adminService.getUsers(clientDTO, tokenDTO);

    assertThat(users).isEqualTo(ResponseEntity.ok(Arrays.asList(user, adminUser)));
  }
}
