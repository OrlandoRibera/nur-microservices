package factories.foodpackage;

import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;

import java.util.List;
import java.util.UUID;

public class CreateFoodPackage implements FoodPackageFactory {
  @Override
  public FoodPackage create(UUID recipeId, UUID clientId, UUID addressId, List<Food> foods, FoodPackageStatus status) {
    return new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.EMPTY);
  }

  @Override
  public FoodPackage create(UUID foodPackageId, UUID recipeId, UUID clientId, UUID addressId, List<Food> foods, FoodPackageStatus status) {
    return new FoodPackage(foodPackageId, recipeId, clientId, addressId, foods, FoodPackageStatus.EMPTY);
  }
}
