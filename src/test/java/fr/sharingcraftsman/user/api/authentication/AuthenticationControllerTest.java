package fr.sharingcraftsman.user.api.authentication;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.client.ClientDTO;
import fr.sharingcraftsman.user.common.DateConverter;
import fr.sharingcraftsman.user.utils.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(AuthenticationController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthenticationControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private AuthenticationService authenticationService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_log_in_and_get_token() throws Exception {
    given(authenticationService.login(any(ClientDTO.class), any(LoginDTO.class))).willReturn(ResponseEntity.ok(TokenDTO.from("john@doe.fr", "aaa", "bbb", DateConverter.fromLocalDateTimeToLong(LocalDateTime.of(2018, Month.JANUARY, 2, 12, 0)))));
    LoginDTO loginDTO = LoginDTO.from("john@doe.fr", "password", true);

    this.mvc.perform(post("/tokens/login")
            .header("client", "client")
            .header("secret", "clientsecret")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(loginDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", not(empty())))
            .andExpect(jsonPath("$.accessToken", not(empty())));
  }

  @Test
  public void should_verify_token() throws Exception {
    given(authenticationService.checkToken(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(get("/tokens/verify")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_log_out() throws Exception {
    given(authenticationService.logout(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(get("/tokens/logout")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_request_for_a_new_access_token() throws Exception {
    given(authenticationService.refreshToken(any(ClientDTO.class), any(TokenDTO.class))).willReturn(ResponseEntity.ok(TokenDTO.from("john@doe.fr", "ddd", "eee", DateConverter.fromLocalDateTimeToLong(LocalDateTime.of(2018, Month.JANUARY, 2, 12, 0)))));

    this.mvc.perform(get("/tokens/refresh-token")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("refresh-token", "bbb"))
            .andExpect(status().isOk());
  }
}
