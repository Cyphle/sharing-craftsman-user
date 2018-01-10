package fr.sharingcraftsman.user.infrastructure.models;

import fr.sharingcraftsman.user.domain.authorization.Group;
import fr.sharingcraftsman.user.domain.authorization.Role;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authorizations")
@ToString
@EqualsAndHashCode
public class AuthorizationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;
  @Column(name = "access_group")
  private String group;
  @Column(name = "role")
  private String role;

  public AuthorizationEntity() {
  }

  public AuthorizationEntity(String group, String role) {
    this.group = group;
    this.role = role;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public static List<Role> fromInfraToDomain(List<AuthorizationEntity> authorizationEntities) {
    return authorizationEntities.stream()
            .map(groupRole -> Role.from(groupRole.getRole()))
            .collect(Collectors.toList());
  }

  public static Set<Group> fromInfraToDomainRolesGroupedByGroup(Iterable<AuthorizationEntity> roles) {
    Set<Group> groups = new HashSet<>();
    roles.forEach(role -> groups.add(Group.from(role.getGroup())));
    roles.forEach(role -> {
      groups.forEach(group -> {
        if (role.getGroup().equals(group.getName()))
          group.addRole(Role.from(role.getRole()));
      });
    });
    return groups;
  }
}
