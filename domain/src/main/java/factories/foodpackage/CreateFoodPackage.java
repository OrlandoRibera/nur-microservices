package factories.foodpackage;

import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;

import java.util.List;
import java.util.UUID;

public class CreateFoodPackage implements FoodPackageFactory {
  @Override
  public FoodPackage create(UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status) {
    return new FoodPackage(recipeId, clientId, address, foods, FoodPackageStatus.NEW);
  }

  @Override
  public FoodPackage create(UUID foodPackageId, UUID recipeId, UUID clientId, String address, List<Food> foods, FoodPackageStatus status) {
    return new FoodPackage(foodPackageId, recipeId, clientId, address, foods, FoodPackageStatus.NEW);
  }
}
