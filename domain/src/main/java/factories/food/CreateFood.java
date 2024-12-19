package factories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodStatus;
import infrastructure.model.FoodType;

import java.util.UUID;

public class CreateFood implements FoodFactory {
  @Override
  public Food create(String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    return null;
  }

  @Override
  public Food create(UUID foodId, String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    return new Food(foodId, name, type, kcal, foodPackageId);
  }
}
