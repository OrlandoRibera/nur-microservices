package infrastructure.repositories.foodpackage;

import infrastructure.model.FoodPackageJpaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodPackageCrudRepository extends CrudRepository<FoodPackageJpaModel, UUID> {
  FoodPackageJpaModel findByRecipeIdAndClientId(UUID recipeId, UUID clientId);
}
