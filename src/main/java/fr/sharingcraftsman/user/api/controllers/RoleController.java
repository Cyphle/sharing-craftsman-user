package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.services.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@Api(description = "Endpoints to manage roles and groups for users")
public class RoleController {
  private RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @ApiOperation(value = "Endpoint to get groups and roles", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return roleService.getAuthorizations(clientDTO, tokenDTO);
  }

  /*
  ASSIGN group endpoint (only authorized for user who has GROUP_ADMIN -> In Admin controller
  Get groups and roles endpoint
   */


  /*
  #  - Should have for user
#    -> GROUP_USERS
#        -> Containing ROLE_USER
#  - Should have for admin
#    -> GROUP_ADMINS
#        -> Containing ROLE_ADMIN, ROLE_USER
   */
}
