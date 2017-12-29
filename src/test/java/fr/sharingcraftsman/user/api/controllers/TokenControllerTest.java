package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.UserApplication;
import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.api.services.TokenService;
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
@WebMvcTest(TokenController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TokenControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private TokenService tokenService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_log_in_and_get_token() throws Exception {
    ZonedDateTime zdt = LocalDateTime.of(2018, Month.JANUARY, 2, 12, 0).atZone(ZoneId.systemDefault());
    OAuthToken oAuthToken = new OAuthToken("john@doe.fr", "aaa", "bbb", zdt.toInstant().toEpochMilli());
    given(tokenService.login(any(OAuthClient.class), any(Login.class))).willReturn(ResponseEntity.ok(oAuthToken));

    Login login = new Login("john@doe.fr", "password", true);

    this.mvc.perform(post("/tokens/login")
            .header("client", "client")
            .header("secret", "clientsecret")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", not(empty())))
            .andExpect(jsonPath("$.accessToken", not(empty())));
  }

  @Test
  public void should_verify_token() throws Exception {
    given(tokenService.checkToken(any(OAuthClient.class), any(OAuthToken.class))).willReturn(ResponseEntity.ok().build());

    OAuthToken token = new OAuthToken();
    token.setUsername("john@doe.fr");
    token.setAccessToken("aaa");

    this.mvc.perform(get("/tokens/verify")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }

  @Test
  public void should_log_out() throws Exception {
    given(tokenService.logout(any(OAuthClient.class), any(OAuthToken.class))).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(get("/tokens/logout")
            .header("client", "client")
            .header("secret", "clientsecret")
            .header("username", "john@doe.fr")
            .header("access-token", "aaa"))
            .andExpect(status().isOk());
  }
}
