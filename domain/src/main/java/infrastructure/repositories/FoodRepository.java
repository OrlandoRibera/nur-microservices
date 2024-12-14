package infrastructure.repositories;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;

import java.util.List;
import java.util.UUID;

public interface FoodRepository {
  UUID update(Food food);

  List<Food> findByFoodPackageId(UUID foodPackageId) throws BusinessRuleValidationException;
}
