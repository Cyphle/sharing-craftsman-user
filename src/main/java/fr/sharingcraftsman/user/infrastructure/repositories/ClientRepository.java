package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.ClientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
  @Query("Select c from ClientEntity c where c.name = ?1 and c.secret = ?2 and c.isActive = true")
  ClientEntity findByNameAndSecret(String name, String secret);
  @Query("Select c from ClientEntity c where c.name = ?1 and c.isActive = true")
  ClientEntity findByName(String name);
}
