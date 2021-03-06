package fr.sharingcraftsman.user.api.authorization;

import com.google.common.collect.Sets;
import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.authentication.TokenDTO;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.utils.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(AuthorizationController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthorizationControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AuthorizationService authorizationService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_get_authorizations_in_groups_and_roles() throws Exception {
    given(authorizationService.getAuthorizations(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok(AuthorizationsDTO.from(Sets.newHashSet(GroupDTO.from("USERS", Sets.newHashSet(RoleDTO.from("ROLE_USER")))))));

    MvcResult mvcResult = this.mvc.perform(get("/roles")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk())
            .andReturn();

    AuthorizationsDTO authorizationsDTO = Mapper.fromJsonStringToObject(mvcResult.getResponse().getContentAsString(), AuthorizationsDTO.class);
    assertThat(authorizationsDTO.getGroups()).contains(GroupDTO.from("USERS", Sets.newHashSet(RoleDTO.from("ROLE_USER"))));
  }
}
