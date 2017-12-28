package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.api.services.LoginService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Api(description = "Endpoints to log in")
public class LoginController {
  private LoginService loginService;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @ApiOperation(value = "Post log in information", response = OAuthToken.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Response with token containing username, client, access token, refresh token and expiration date"),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public ResponseEntity logIn(@RequestBody Login login) {
    return loginService.login(login);
  }
}
