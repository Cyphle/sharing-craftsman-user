package fr.sharingcraftsman.user.domain.client;

import fr.sharingcraftsman.user.domain.client.exceptions.ClientException;
import fr.sharingcraftsman.user.domain.client.ports.ClientRepository;
import fr.sharingcraftsman.user.domain.utils.SecretGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientOrganisationTest {
  private ClientOrganisationImpl clientOrganisationImpl;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private SecretGenerator secretGenerator;

  @Before
  public void setUp() throws Exception {
    clientOrganisationImpl = new ClientOrganisationImpl(clientRepository, secretGenerator);
  }

  @Test
  public void should_validate_client() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(Client.from("client", "clientsecret"));

    Client client = Client.from("client", "clientsecret");

    assertThat(clientOrganisationImpl.doesClientExist(client)).isTrue();
  }

  @Test
  public void should_return_false_if_client_does_not_exists() throws Exception {
    given(clientRepository.findClient(any(Client.class))).willReturn(UnknownClient.get());

    Client client = Client.from("client", "clientsecret");

    assertThat(clientOrganisationImpl.doesClientExist(client)).isFalse();
  }

  @Test
  public void should_create_new_client() throws Exception {
    given(secretGenerator.generateSecret()).willReturn("secret");
    given(clientRepository.findClientByName(any(Client.class))).willReturn(UnknownClient.get());

    clientOrganisationImpl.createNewClient(Client.from("sharingcraftsman", ""));

    verify(clientRepository).createClient(Client.from("sharingcraftsman", "secret"));
  }

  @Test
  public void should_throw_already_existing_client_if_client_already_exists() throws Exception {
    given(secretGenerator.generateSecret()).willReturn("secret");
    given(clientRepository.findClientByName(any(Client.class))).willReturn(Client.from("sharingcraftsman", "secret"));

    try {
      clientOrganisationImpl.createNewClient(Client.from("sharingcraftsman", ""));
      fail("Should throw already existing client exception");
    } catch (ClientException e) {
      assertThat(e.getMessage()).isEqualTo("Already existing client");
    }
  }
}
