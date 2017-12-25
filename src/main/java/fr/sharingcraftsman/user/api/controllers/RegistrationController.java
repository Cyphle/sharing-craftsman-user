package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class RegistrationController {
  private RegistrationService registrationService;

  @Autowired
  public RegistrationController(RegistrationService registrationService) {
    this.registrationService = registrationService;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/register")
  public ResponseEntity registerUser(@RequestBody Login login) {
    return registrationService.registerUser(login);
  }
}
