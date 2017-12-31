package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.api.models.ClientDTO;
import fr.sharingcraftsman.user.api.models.TokenDTO;
import fr.sharingcraftsman.user.api.services.AdminService;
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
@RequestMapping("/admin")
@Api(description = "Endpoints to for admin")
public class AdminController {
  private AdminService adminService;

  @Autowired
  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @ApiOperation(value = "Endpoint to get users", response = ResponseEntity.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = ""),
          @ApiResponse(code = 401, message = "Unauthorized")
  })
  @RequestMapping(method = RequestMethod.GET, value = "/users")
  public ResponseEntity verify(@RequestHeader("client") String client,
                               @RequestHeader("secret") String secret,
                               @RequestHeader("username") String username,
                               @RequestHeader("access-token") String accessToken) {
    ClientDTO clientDTO = new ClientDTO(client, secret);
    TokenDTO tokenDTO = new TokenDTO(username, accessToken);
    return adminService.getUsers(clientDTO, tokenDTO);
  }
  /*
  - Get list of users with profiles -> OK
  - Add user
  - Remove user
  - Deactivate user
  - Modify user ?
  - Get list of roles, groups
  - Add/remove roles groups (impact on user groups)
  - Get list of authorizations
  - Add/remove authorizations
  - Get list of tokens
  - Remove tokens
   */
}
