package mappers;

import core.BusinessRuleValidationException;
import dto.FoodDTO;
import infrastructure.model.Food;
import infrastructure.model.FoodType;

import java.util.UUID;

public final class FoodMapper {
  public static FoodDTO from(Food food) {
    return new FoodDTO(
      food.getName(),
      food.getStatus().name(),
      food.getType().name(),
      food.getKcal(),
      food.getFoodPackageId().toString()
    );
  }

  public static Food from(FoodDTO foodDTO) throws BusinessRuleValidationException {
    FoodType foodType = FoodType.valueOf(foodDTO.type());
    return new Food(foodDTO.name(), foodType, foodDTO.kcal(), UUID.fromString(foodDTO.foodPackageId()));
  }
}
