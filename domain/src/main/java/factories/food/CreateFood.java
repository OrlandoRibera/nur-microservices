package factories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodType;

import java.util.UUID;

public class CreateFood implements FoodFactory {

  @Override
  public Food create(String name, FoodType type, float kcal, UUID foodPackageId) throws BusinessRuleValidationException {
    return new Food(name, type, kcal, foodPackageId);
  }
}
