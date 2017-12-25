package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.ClientRegistration;
import fr.sharingcraftsman.user.api.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {
  private ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public ResponseEntity registerUser(@RequestBody ClientRegistration clientRegistration) {
    if (clientRegistration.getName().equals("sharingcraftsman"))
      return clientService.register(clientRegistration);
    else
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
  }
}
