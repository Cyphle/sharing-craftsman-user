package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.api.services.LoginService;
import fr.sharingcraftsman.user.api.services.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
@Api(description = "Endpoints to manage token")
public class TokenController {
  private TokenService tokenService;

  @Autowired
  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @ApiOperation(value = "Post log in information", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/verify")
  public ResponseEntity logIn(@RequestBody OAuthToken token) {
    return tokenService.checkToken(token);
  }
}
