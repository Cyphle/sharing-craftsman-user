package fr.sharingcraftsman.user.acceptance.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminProfileDsl {
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String website;
  private String github;
  private String linkedin;
  private boolean isActive;
  private String authorizations;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getGithub() {
    return github;
  }

  public void setGithub(String github) {
    this.github = github;
  }

  public String getLinkedin() {
    return linkedin;
  }

  public void setLinkedin(String linkedin) {
    this.linkedin = linkedin;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public String getAuthorizations() {
    return authorizations;
  }

  public void setAuthorizations(String authorizations) {
    this.authorizations = authorizations;
  }

  public AuthorizationDsl getAuthorizationAsDsl() {
    String[] groups = authorizations.split(";");
    List<GroupDsl> groupsDsl = new ArrayList<>();

    Arrays.stream(groups)
            .forEach(group -> {
              Pattern groupPattern = Pattern.compile("^(.*)=");
              Matcher groupMatcher = groupPattern.matcher(group);
              if (groupMatcher.find()) {
                GroupDsl groupDsl = new GroupDsl(groupMatcher.group(1));

                Pattern rolesPattern = Pattern.compile("=(.*)$");
                Matcher rolesMatcher = rolesPattern.matcher(group);
                rolesMatcher.find();
                List<String> roles = Arrays.asList(rolesMatcher.group(1).split(","));
                roles.forEach(role -> groupDsl.addRole(new RoleDsl(role)));

                groupsDsl.add(groupDsl);
              }
            });

    AuthorizationDsl authorizationDsl = new AuthorizationDsl();
    authorizationDsl.setGroups(groupsDsl);

    return authorizationDsl;
  }
}
