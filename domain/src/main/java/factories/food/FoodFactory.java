package factories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodStatus;
import infrastructure.model.FoodType;

import java.util.UUID;

public interface FoodFactory {
  Food create(String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException;
  Food create(UUID foodId, String name, FoodType type, FoodStatus foodStatus, float kcal, UUID foodPackageId) throws BusinessRuleValidationException;
}
