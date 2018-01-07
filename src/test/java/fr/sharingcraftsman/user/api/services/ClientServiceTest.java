package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
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
    given(clientRepository.findClientByName(any(Client.class))).willReturn(Client.unkownClient());
    given(clientRepository.createClient(any(Client.class))).willReturn(Client.knownClient("sharingcraftsman", "secret"));
    ClientDTO client = new ClientDTO();
    client.setName("sharingcraftsman");

    ResponseEntity response = clientService.register(client);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
