package fr.sharingcraftsman.user.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.*;
import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import fr.sharingcraftsman.user.infrastructure.models.UserGroup;
import fr.sharingcraftsman.user.infrastructure.repositories.GroupRoleRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserGroupRepository;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminStepsDef extends SpringAcceptanceTestConfig {
  @Autowired
  private GroupRoleRepository groupRoleRepository;
  @Autowired
  private UserGroupRepository userGroupRepository;

  @Before
  public void setUp() {
    if (this.mvc == null) {
      this.mvc = MockMvcBuilders
              .webAppContextSetup(context)
              .build();
    }
  }

  @Given("^The client <(.*)> is registered$")
  public void createClient(String clientName) throws Exception {
    ClientDTO client = new ClientDTO();
    client.setName(clientName);

    this.mvc
            .perform(post(getBaseUri() + "/clients/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(client))
            );
  }

  @And("^An admin group is created with admin role$")
  public void createAdminGroup() {
    groupRoleRepository.save(new GroupRole("ADMINS", "ROLE_ADMIN"));
  }

  @And("^I have registered an admin account with username <(.*)> and password <(.*)>$")
  public void registerAdmin(String username, String password) throws Exception {
    LoginDsl login = new LoginDsl(username, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/users/register")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();

    userGroupRepository.save(new UserGroup(username, "ADMINS"));
  }

  @And("^I am connected with my account <(.*)> and password <(.*)>$")
  public void connect(String username, String password) throws Exception {
    login = new LoginDsl(username, password);
    response = this.mvc
            .perform(post(getBaseUri() + "/tokens/login")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(login))
            )
            .andExpect(status().isOk())
            .andReturn();

    token = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), TokenDsl.class);
  }

  @And("^I create the following authorizations$")
  public void createAuthorizations(List<GroupRoleDsl> groupsRoleDsl) throws Exception {
    groupsRoleDsl.forEach(groupRoleDsl -> {
      GroupDsl groupDsl = new GroupDsl();
      groupDsl.setName(groupRoleDsl.getGroup());
      groupDsl.setRoles(new HashSet<>(Collections.singletonList(new RoleDsl(groupRoleDsl.getRole()))));

      try {
        this.mvc
                .perform(post(getBaseUri() + "/admin/roles/groups")
                        .header("client", "sharingcraftsman")
                        .header("secret", "secret")
                        .header("username", login.getUsername())
                        .header("access-token", token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Mapper.fromObjectToJsonString(groupDsl))
                )
                .andExpect(status().isOk())
                .andReturn();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @When("^I delete the role <(.*)> from the group <(.*)>$")
  public void deleteAuthorization(String role, String group) throws Exception {
    GroupDsl groupDsl = new GroupDsl();
    groupDsl.setName(group);
    groupDsl.setRoles(new HashSet<>(Collections.singletonList(new RoleDsl(role))));

    this.mvc
            .perform(delete(getBaseUri() + "/admin/roles/groups")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mapper.fromObjectToJsonString(groupDsl))
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @And("^I consult all the groups and roles$")
  public void consultAllGroupsWithRoles() throws Exception {
    response = this.mvc.perform(get(getBaseUri() + "/admin/roles/groups")
                    .header("client", "sharingcraftsman")
                    .header("secret", "secret")
                    .header("username", login.getUsername())
                    .header("access-token", token.getAccessToken())
            )
            .andExpect(status().isOk())
            .andReturn();
  }

  @Then("^I get the roles$")
  public void checkGroupsAndRoles(List<GroupRoleDsl> groupsRoleDsl) throws IOException {
    Set<GroupDsl> expectedGroups = groupsRoleDsl.stream()
            .map(group -> new GroupDsl(group.getGroup()))
            .collect(Collectors.toSet());
    groupsRoleDsl.forEach(group -> {
      expectedGroups.forEach(expectedGroup -> {
        if (expectedGroup.getName().equals(group.getGroup())) {
          expectedGroup.addRole(new RoleDsl(group.getRole()));
        }
      });
    });

    Set<GroupDsl> groups = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), Set.class, GroupDsl.class);

    assertThat(groups).isEqualTo(expectedGroups);
  }
}
