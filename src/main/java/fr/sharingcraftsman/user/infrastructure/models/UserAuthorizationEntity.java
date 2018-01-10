package fr.sharingcraftsman.user.infrastructure.models;

import com.google.common.collect.Lists;
import fr.sharingcraftsman.user.domain.authorization.Group;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_authorizations")
@ToString
@EqualsAndHashCode
public class UserAuthorizationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "username")
  private String username;
  @Column(name = "access_group")
  private String group;

  public UserAuthorizationEntity() {
  }

  public UserAuthorizationEntity(String username, String group) {
    this.username = username;
    this.group = group;
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

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public static List<Group> fromInfraToDomain(List<UserAuthorizationEntity> userAuthorizationEntities) {
    return userAuthorizationEntities.stream()
            .map(group -> Group.from(group.getGroup()))
            .collect(Collectors.toList());
  }

  public static AuthorizationEntity fromDomainToInfra(Group group) {
    return new AuthorizationEntity(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getName());
  }
}
