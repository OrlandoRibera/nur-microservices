package factories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodType;

import java.util.UUID;

public interface FoodFactory {
  Food create(String name, FoodType type, float kcal, UUID foodPackageId) throws BusinessRuleValidationException;
}
