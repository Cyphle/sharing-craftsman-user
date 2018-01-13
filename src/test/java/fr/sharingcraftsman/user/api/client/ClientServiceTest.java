package fr.sharingcraftsman.user.api.client;

import fr.sharingcraftsman.user.domain.client.Client;
import fr.sharingcraftsman.user.domain.client.UnknownClient;
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
    given(clientRepository.findClientByName(any(Client.class))).willReturn(UnknownClient.get());
    given(clientRepository.createClient(any(Client.class))).willReturn(Client.from("sharingcraftsman", "secret"));
    ClientDTO client = ClientDTO.from("sharingcraftsman");

    ResponseEntity response = clientService.register(client);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
