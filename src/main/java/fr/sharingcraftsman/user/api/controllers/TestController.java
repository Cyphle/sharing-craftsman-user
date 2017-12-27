package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class TestController {
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @RequestMapping(method = {RequestMethod.GET}, value = {"/version/{username}/{password}"})
  public String getVersion(@PathVariable String username, @PathVariable String password) {
    log.info("TEST");
    log.info("Username: " + username);
    log.info("Password: " + password);
    log.info("TEST");
    return "{username: " + username + ", password: " + password + "}";
  }

  @RequestMapping(method = {RequestMethod.POST}, value = {"/test"})
  public String testPost(@RequestBody Credentials credentials) {
    Credentials temp = credentials;

    return "OK";
  }
}
