package fr.sharingcraftsman.user.api.controllers;

import fr.sharingcraftsman.user.domain.authentication.Credentials;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class TestController {
  @RequestMapping(method = {RequestMethod.GET}, value = {"/version"})
  public String getVersion() {
    return "1.0";
  }

  @RequestMapping(method = {RequestMethod.POST}, value = {"/test"})
  public String testPost(@RequestBody Credentials credentials) {
    Credentials temp = credentials;

    return "OK";
  }
}
