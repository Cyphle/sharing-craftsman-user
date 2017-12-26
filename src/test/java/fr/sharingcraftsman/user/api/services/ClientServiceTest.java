package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientRegistration;
import fr.sharingcraftsman.user.infrastructure.models.OAuthClient;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {
  @Mock
  private ClientRepository clientRepository;

  private ClientService clientService;

  @Before
  public void setUp() throws Exception {
    clientService = new ClientService(clientRepository);
  }

  @Test
  public void should_register_new_client() throws Exception {
    given(clientRepository.save(any(OAuthClient.class))).willReturn(new OAuthClient("sharingcraftsman", "secret"));
    ClientRegistration client = new ClientRegistration();
    client.setName("sharingcraftsman");

    ResponseEntity response = clientService.register(client);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
