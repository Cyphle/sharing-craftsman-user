package fr.sharingcraftsman.user.infrastructure.models;

import fr.sharingcraftsman.user.domain.admin.UserInfoOld;
import fr.sharingcraftsman.user.domain.authentication.exceptions.CredentialsException;
import fr.sharingcraftsman.user.domain.common.*;
import fr.sharingcraftsman.user.domain.user.Profile;
import fr.sharingcraftsman.user.domain.user.User;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@ToString
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username", unique = true)
  private String username;
  @Column(name = "lastname")
  private String lastname;
  @Column(name = "firstname")
  private String firstname;
  @Column(name = "password")
  private String password;
  @Column(name = "email")
  private String email;
  @Column(name = "website")
  private String website;
  @Column(name = "github")
  private String github;
  @Column(name = "linkedin")
  private String linkedin;
  @Column(name = "is_active")
  private boolean isActive = true;
  @Column(name = "creation_date")
  private Date creationDate;
  @Column(name = "last_update_date")
  private Date lastUpdateDate;

  public UserEntity() { }

  public UserEntity(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public UserEntity(String username, String firstname, String lastname, String email, String website, String github, String linkedin) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.website = website;
    this.github = github;
    this.linkedin = linkedin;
  }

  public UserEntity(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin) {
    this(username, firstname, lastname, email, website, github, linkedin);
    this.password = password;
  }

  public UserEntity(String username, String password, String firstname, String lastname, String email, String website, String github, String linkedin, boolean isActive, Date creationDate, Date lastUpdateDate) {
    this(username, password, firstname, lastname, email, website, github, linkedin);
    this.isActive = isActive;
    this.creationDate = creationDate;
    this.lastUpdateDate = lastUpdateDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public void updateFromProfile(UserEntity userEntity) {
    firstname = userEntity.firstname;
    lastname = userEntity.lastname;
    email = userEntity.email;
    website = userEntity.website;
    github = userEntity.github;
    linkedin = userEntity.linkedin;
  }

  public void updateFromAdminUser(UserInfoOld user) {
    username = user.getUsernameContent();
    firstname = user.getFirstname();
    lastname = user.getLastname();
    email = user.getEmail();
    website = user.getWebsite();
    github = user.getGithub();
    linkedin = user.getLinkedin();
  }

  public static UserEntity fromDomainToInfra(User user) {
    return new UserEntity(user.getUsernameContent(), user.getPasswordContent());
  }

  public static User fromInfraToDomain(UserEntity userEntity) throws CredentialsException {
    return User.from(
            userEntity.getUsername(),
            userEntity.getPassword() != null ? userEntity.getPassword() : "UNSET"
    );
  }

  public static UserEntity fromDomainToInfra(UserInfoOld user) {
    return new UserEntity(
            user.getUsernameContent(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getWebsite(),
            user.getGithub(),
            user.getLinkedin()
    );
  }

  public static List<UserInfoOld> fromInfraToAdminDomain(List<UserEntity> userEntities) {
    return userEntities.stream()
            .map(user -> UserInfoOld.from(
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getWebsite(),
                    user.getGithub(),
                    user.getLinkedin(),
                    user.isActive(),
                    user.getCreationDate(),
                    user.getLastUpdateDate()
            ))
            .collect(Collectors.toList());
  }

  public static UserInfoOld fromInfraToAdminDomain(UserEntity userEntity) {
    return UserInfoOld.from(
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getFirstname(),
            userEntity.getLastname(),
            userEntity.getEmail(),
            userEntity.getWebsite(),
            userEntity.getGithub(),
            userEntity.getLinkedin(),
            userEntity.isActive(),
            userEntity.getCreationDate(),
            userEntity.getLastUpdateDate());
  }

  public static Profile fromInfraToDomainProfile(UserEntity userEntity) throws UsernameException {
    return Profile.from(
            Username.from(userEntity.getUsername()),
            Name.of(userEntity.getFirstname()),
            Name.of(userEntity.getLastname()),
            Email.from(userEntity.getEmail()),
            Link.to(userEntity.getWebsite()),
            Link.to(userEntity.getGithub()),
            Link.to(userEntity.getLinkedin()));
  }

  public static UserEntity fromDomainToInfraProfile(Profile profile) {
    return new UserEntity(profile.getUsernameContent(), profile.getFirstnameContent(), profile.getLastnameContent(), profile.getEmailContent(), profile.getWebsiteContent(), profile.getGithubContent(), profile.getLinkedinContent());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserEntity userEntity = (UserEntity) o;

    if (username != null ? !username.equals(userEntity.username) : userEntity.username != null) return false;
    if (lastname != null ? !lastname.equals(userEntity.lastname) : userEntity.lastname != null) return false;
    if (firstname != null ? !firstname.equals(userEntity.firstname) : userEntity.firstname != null) return false;
    return password != null ? password.equals(userEntity.password) : userEntity.password == null;
  }

  @Override
  public int hashCode() {
    int result = username != null ? username.hashCode() : 0;
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}
