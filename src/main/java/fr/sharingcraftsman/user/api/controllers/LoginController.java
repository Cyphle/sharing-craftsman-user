package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class LoginController {
  private LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public ResponseEntity registerUser(@RequestBody Login login) {
    return loginService.login(login);
  }
}
