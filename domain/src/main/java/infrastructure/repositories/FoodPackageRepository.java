package infrastructure.repositories;

import core.BusinessRuleValidationException;
import infrastructure.model.FoodPackage;

import java.util.UUID;

public interface FoodPackageRepository {
  UUID create(FoodPackage foodPackage);
  UUID update(FoodPackage foodPackage);

  FoodPackage get(UUID id) throws BusinessRuleValidationException;

  FoodPackage findByRecipeIdAndClientId(UUID recipeId, UUID clientId) throws BusinessRuleValidationException;
}
