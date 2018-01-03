package fr.sharingcraftsman.user.infrastructure.repositories;

import fr.sharingcraftsman.user.infrastructure.models.GroupRole;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<GroupRole, Long> {
}
