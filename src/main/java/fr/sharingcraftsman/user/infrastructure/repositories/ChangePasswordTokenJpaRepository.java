package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.ChangePasswordTokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChangePasswordTokenJpaRepository extends CrudRepository<ChangePasswordTokenEntity, Long> {
  ChangePasswordTokenEntity findByUsername(String username);
}
