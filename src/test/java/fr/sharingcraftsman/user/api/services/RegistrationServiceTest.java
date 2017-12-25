package fr.sharingcraftsman.user.api.services;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.infrastructure.adapters.DateService;
import fr.sharingcraftsman.user.infrastructure.models.ApiClient;
import fr.sharingcraftsman.user.infrastructure.models.User;
import fr.sharingcraftsman.user.infrastructure.repositories.ClientRepository;
import fr.sharingcraftsman.user.infrastructure.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private DateService dateService;

  private RegistrationService registrationService;

  @Before
  public void setUp() throws Exception {
    given(dateService.now()).willReturn(Date.from(LocalDateTime.of(2017, Month.DECEMBER, 24, 12, 0).atZone(ZoneId.systemDefault()).toInstant()));
    registrationService = new RegistrationService(userRepository, clientRepository, dateService);
  }

  @Test
  public void should_register_user() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "clientsecret")).willReturn(new ApiClient("client", "clientsecret"));
    Login login = new Login("client", "clientsecret", "john@doe.fr", "password");

    ResponseEntity response = registrationService.registerUser(login);

    User expectedUser = new User("john@doe.fr", "T49xWf/l7gatvfVwethwDw==");
    expectedUser.setCreationDate(dateService.now());
    expectedUser.setLastUpdateDate(dateService.now());
    verify(userRepository).save(expectedUser);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_get_invalid_credential_username_when_username_is_not_specified() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "clientsecret")).willReturn(new ApiClient("client", "clientsecret"));
    Login login = new Login("client", "clientsecret", "", "password");

    ResponseEntity response = registrationService.registerUser(login);

    verify(userRepository, never()).save(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Username cannot be empty");
  }

  @Test
  public void should_get_invalid_credential_password_when_username_is_not_specified() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "clientsecret")).willReturn(new ApiClient("client", "clientsecret"));
    Login login = new Login("client", "clientsecret", "john@doe.fr", "");

    ResponseEntity response = registrationService.registerUser(login);

    verify(userRepository, never()).save(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Password cannot be empty");
  }

  @Test
  public void should_get_user_already_exists_when_using_already_existing_username() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "clientsecret")).willReturn(new ApiClient("client", "clientsecret"));
    given(userRepository.findByUsername("john@doe.fr")).willReturn(new User("john@doe.fr", "password"));
    Login login = new Login("client", "clientsecret", "john@doe.fr", "password");

    ResponseEntity response = registrationService.registerUser(login);

    verify(userRepository, never()).save(any(User.class));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo("Collaborator already exists with username: john@doe.fr");
  }

  @Test
  public void should_get_unknown_client_response_when_client_is_not_known() throws Exception {
    given(clientRepository.findByNameAndSecret("client", "clientsecret")).willReturn(null);
    Login login = new Login("client", "clientsecret", "john@doe.fr", "password");

    ResponseEntity response = registrationService.registerUser(login);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo("Unknown client");
  }
}
