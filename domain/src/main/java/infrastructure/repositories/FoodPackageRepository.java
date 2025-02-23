package infrastructure.repositories;

import core.BusinessRuleValidationException;
import infrastructure.model.FoodPackage;

import java.util.List;
import java.util.UUID;

public interface FoodPackageRepository {
  UUID create(FoodPackage foodPackage);

  UUID update(FoodPackage foodPackage);

  List<FoodPackage> getAll();

  FoodPackage get(UUID id) throws BusinessRuleValidationException;

  FoodPackage findByRecipeIdAndClientId(UUID recipeId, UUID clientId) throws BusinessRuleValidationException;
}
