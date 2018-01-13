package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.AuthorizationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorizationJpaRepository extends CrudRepository<AuthorizationEntity, Long> {
  @Query("Select g from AuthorizationEntity g where g.group = ?1")
  List<AuthorizationEntity> findByGroup(String group);
  @Query("Select g from AuthorizationEntity g where g.group = ?1 and g.role = ?2")
  AuthorizationEntity findFromGroupNameAndRole(String group, String role);
}
