package fr.sharingcraftsman.user.api.client;

import fr.sharingcraftsman.user.UserApplication;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UserApplication.class})
@WebMvcTest(ClientController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ClientControllerTest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private ClientService clientService;

  @Before
  public void setup() {
    this.mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
  }

  @Test
  public void should_register_client() throws Exception {
    ClientDTO client = new ClientDTO();
    client.setName("sharingcraftsman");
    given(clientService.register(client)).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(post("/clients/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(client)))
            .andExpect(status().isOk());
  }

  @Test
  public void should_return_unauthorized_if_client_name_is_not_correct() throws Exception {
    ClientDTO client = new ClientDTO();
    client.setName("toto");
    given(clientService.register(client)).willReturn(ResponseEntity.ok().build());

    this.mvc.perform(post("/clients/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Mapper.fromObjectToJsonString(client)))
            .andExpect(status().isUnauthorized());
  }
}
