package infrastructure.repositories.user;


import infrastructure.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCrudRepository extends CrudRepository<User, UUID> {
}
