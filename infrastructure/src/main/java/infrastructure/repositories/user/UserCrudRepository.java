package infrastructure.repositories.user;


import infrastructure.model.UserJpaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCrudRepository extends CrudRepository<UserJpaModel, String> {
}
