package fr.sharingcraftsman.user.acceptance;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.sharingcraftsman.user.acceptance.config.SpringAcceptanceTestConfig;
import fr.sharingcraftsman.user.acceptance.dsl.*;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.models.UserAuthorizationEntity;
import fr.sharingcraftsman.user.infrastructure.models.UserEntity;
import fr.sharingcraftsman.user.infrastructure.repositories.AuthorizationJpaRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserAuthorizationJpaRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserJpaRepository;
import fr.sharingcraftsman.user.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminStepsDef extends SpringAcceptanceTestConfig {
  @Autowired
  private AuthorizationJpaRepository authorizationJpaRepository;
  @Autowired
  private UserAuthorizationJpaRepository userAuthorizationJpaRepository;

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
    authorizationJpaRepository.save(new AuthorizationEntity("ADMINS", "ROLE_ADMIN"));
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

    userAuthorizationJpaRepository.save(new UserAuthorizationEntity(username, "ADMINS"));
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

  @Given("^I create the following authorizations$")
  public void createAuthorizations(List<GroupRoleDsl> groupsRoleDsl) throws Exception {
    groupsRoleDsl.forEach(groupRoleDsl -> {
      GroupDsl groupDsl = new GroupDsl();
      groupDsl.setName(groupRoleDsl.getGroup());
      groupDsl.setRoles(new HashSet<>(Collections.singletonList(new RoleDsl(groupRoleDsl.getRole()))));

      try {
        this.mvc.perform(post(getBaseUri() + "/admin/roles/groups")
                .header("client", "sharingcraftsman")
                .header("secret", "secret")
                .header("username", login.getUsername())
                .header("access-token", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Mapper.fromObjectToJsonString(groupDsl)))
                .andExpect(status().isOk())
                .andReturn();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @Given("^I create the following users$")
  public void createUsers(List<UserDsl> users) {
    users.forEach(userDsl -> {
      try {
        this.mvc.perform(post(getBaseUri() + "/admin/users")
                .header("client", "sharingcraftsman")
                .header("secret", "secret")
                .header("username", login.getUsername())
                .header("access-token", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Mapper.fromObjectToJsonString(userDsl)))
                .andExpect(status().isOk())
                .andReturn();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @When("^I update users$")
  public void updateUsers(List<UserDsl> users) throws Exception {
    users.forEach(user -> {
      try {
        this.mvc.perform(put(getBaseUri() + "/admin/users")
                .header("client", "sharingcraftsman")
                .header("secret", "secret")
                .header("username", login.getUsername())
                .header("access-token", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Mapper.fromObjectToJsonString(user)))
                .andExpect(status().isOk())
                .andReturn();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @And("^I delete user <(.*)>$")
  public void deleteUser(String username) throws Exception {
    this.mvc.perform(delete(getBaseUri() + "/admin/users/" + username)
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken()))
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("^I delete the role <(.*)> from the group <(.*)>$")
  public void deleteAuthorization(String role, String group) throws Exception {
    GroupDsl groupDsl = new GroupDsl();
    groupDsl.setName(group);
    groupDsl.setRoles(new HashSet<>(Collections.singletonList(new RoleDsl(role))));

    this.mvc.perform(delete(getBaseUri() + "/admin/roles/groups")
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(groupDsl)))
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("^I consult all users profiles$")
  public void getAllUsers() throws Exception {
    response = this.mvc.perform(get(getBaseUri() + "/admin/users")
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken()))
            .andExpect(status().isOk())
            .andReturn();
  }

  @When("^I add authorization <(.*)> to <(.*)>$")
  public void addAuthorization(String group, String username) throws Exception {
    UserGroupDsl userGroup = new UserGroupDsl(username, group);

    this.mvc.perform(post(getBaseUri() + "/admin/users/groups")
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(userGroup)))
            .andExpect(status().isOk())
            .andReturn();
  }

  @And("^I remove authorization <(.*)> to <(.*)>$")
  public void removeAuthorization(String group, String username) throws Exception {
    UserGroupDsl userGroup = new UserGroupDsl(username, group);

    this.mvc.perform(delete(getBaseUri() + "/admin/users/groups")
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(userGroup)))
            .andExpect(status().isOk())
            .andReturn();
  }

  @And("^I consult all the groups and roles$")
  public void consultAllGroupsWithRoles() throws Exception {
    response = this.mvc.perform(get(getBaseUri() + "/admin/roles/groups")
            .header("client", "sharingcraftsman")
            .header("secret", "secret")
            .header("username", login.getUsername())
            .header("access-token", token.getAccessToken()))
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

  @Then("^I get the profiles$")
  public void checkProfiles(List<AdminProfileDsl> expectedProfiles) throws IOException {
    List<UserDsl> expectedUsers = expectedProfiles.stream()
            .map(profile -> {
              UserDsl user = new UserDsl();
              user.setUsername(profile.getUsername());
              user.setFirstname(profile.getFirstname());
              user.setLastname(profile.getLastname());
              user.setEmail(profile.getEmail());
              user.setWebsite(profile.getWebsite());
              user.setGithub(profile.getGithub());
              user.setLinkedin(profile.getLinkedin());
              user.setActive(profile.isActive());
              user.setAuthorizations(profile.getAuthorizationAsDsl());
              return user;
            })
            .collect(Collectors.toList());

    List<UserDsl> fetchedUsers = Mapper.fromJsonStringToObject(response.getResponse().getContentAsString(), List.class, UserDsl.class);
    fetchedUsers = fetchedUsers.stream()
            .filter(user -> !((UserDsl) user).getUsername().equals("admin@scuser.fr"))
            .collect(Collectors.toList());

    assertThat(fetchedUsers).isEqualTo(expectedUsers);
  }
}
