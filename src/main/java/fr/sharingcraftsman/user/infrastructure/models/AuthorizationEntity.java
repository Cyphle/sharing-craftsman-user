package fr.sharingcraftsman.user.infrastructure.models;

import com.google.common.collect.Lists;
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

  private AuthorizationEntity() {
  }

  private AuthorizationEntity(String group, String role) {
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

  public static AuthorizationEntity from(String group, String role) {
    return new AuthorizationEntity(group, role);
  }

  public static List<Role> fromInfraToDomain(List<AuthorizationEntity> authorizationEntities) {
    return authorizationEntities.stream()
            .map(groupRole -> Role.from(groupRole.getRole()))
            .collect(Collectors.toList());
  }

  public static AuthorizationEntity fromDomainToInfra(Group group) {
    return AuthorizationEntity.from(group.getName(), Lists.newArrayList(group.getRoles()).get(0).getName());
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
