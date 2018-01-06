package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRoleRepository extends CrudRepository<GroupRole, Long> {
  @Query("Select g from GroupRole g where g.group = ?1")
  List<GroupRole> findByGroup(String group);
  @Query("Select g from GroupRole g where g.group = ?1 and g.role = ?2")
  GroupRole findFromGroupNameAndRole(String group, String role);
}
