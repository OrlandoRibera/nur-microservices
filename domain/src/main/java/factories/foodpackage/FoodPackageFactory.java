package factories.foodpackage;

import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;

import java.util.List;
import java.util.UUID;

public interface FoodPackageFactory {
  FoodPackage create(UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status);

  FoodPackage create(UUID foodPackageId, UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status);
}
