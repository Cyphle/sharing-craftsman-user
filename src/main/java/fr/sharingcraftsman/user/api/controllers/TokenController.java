package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.Login;
import fr.sharingcraftsman.user.api.models.OAuthClient;
import fr.sharingcraftsman.user.api.models.OAuthToken;
import fr.sharingcraftsman.user.api.services.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tokens")
@Api(description = "Endpoints to manage token")
public class TokenController {
  private TokenService tokenService;

  @Autowired
  public TokenController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @ApiOperation(value = "Post log in information", response = OAuthToken.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Response with token containing username, client, access token, refresh token and expiration date"),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/login")
  public ResponseEntity logIn(@RequestHeader("client") String client,
                              @RequestHeader("secret") String secret,
                              @RequestBody Login login) {
    OAuthClient oAuthClient = new OAuthClient(client, secret);
    return tokenService.login(oAuthClient, login);
  }

  @ApiOperation(value = "Verify token validity", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/verify")
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    OAuthClient oAuthClient = new OAuthClient(client, secret);
    OAuthToken oAuthToken = new OAuthToken(username, accessToken);
    return tokenService.checkToken(oAuthClient, oAuthToken);
  }

  @ApiOperation(value = "Logout - Delete token", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/logout")
  public ResponseEntity logOut(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    OAuthClient oAuthClient = new OAuthClient(client, secret);
    OAuthToken oAuthToken = new OAuthToken(username, accessToken);
    return tokenService.logout(oAuthClient, oAuthToken);
  }
}
