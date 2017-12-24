package fr.sharingcraftsman.user.command.repositories;


import fr.sharingcraftsman.user.command.common.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
