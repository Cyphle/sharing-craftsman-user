package fr.sharingcraftsman.user.api.admin;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.utils.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(UserAuthorizationAdminController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserEntityAuthorizationAdminControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private UserAuthorizationAdminService userAuthorizationAdminService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_add_group_to_user() throws Exception {
    UserGroupDTO newGroupForUser = new UserGroupDTO("hello@world.fr", "USERS");

    this.mvc.perform(post("/admin/users/groups")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(newGroupForUser)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_remove_group_from_user() throws Exception {
    UserGroupDTO newGroupForUser = new UserGroupDTO("hello@world.fr", "USERS");

    this.mvc.perform(delete("/admin/users/groups")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(newGroupForUser)))
            .andExpect(status().isOk());
  }
}
