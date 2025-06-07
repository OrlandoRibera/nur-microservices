package infrastructure.repositories.food;

import infrastructure.model.FoodJpaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodCrudRepository extends CrudRepository<FoodJpaModel, UUID> {
	List<FoodJpaModel> findByFoodPackageId(UUID foodPackageId);
}
