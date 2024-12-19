package infrastructure.utils;

import annotations.Generated;
import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodJpaModel;
import infrastructure.model.FoodPackageJpaModel;
import infrastructure.model.FoodType;

import java.util.Collections;
import java.util.List;

@Generated
public class FoodUtils {
  public static FoodJpaModel foodToJpaEntity(Food food, FoodPackageJpaModel foodPackageJpaModel) {
    FoodJpaModel model = new FoodJpaModel();
    model.setId(food.getId());
    model.setName(food.getName());
    model.setType(food.getType().toString());
    model.setStatus(food.getStatus().toString());
    model.setKcal(food.getKcal());
    model.setFoodPackage(foodPackageJpaModel);
    return model;
  }

  public static List<FoodJpaModel> foodToJpaEntities(List<Food> foods, FoodPackageJpaModel foodPackageJpaModel) {
    if (foods == null) return Collections.emptyList();
    return foods.stream().map((Food food) -> foodToJpaEntity(food, foodPackageJpaModel)).toList();
  }

  public static Food jpaToFood(FoodJpaModel jpaModel) throws BusinessRuleValidationException {
    return new Food(jpaModel.getId(), jpaModel.getName(), FoodType.valueOf(jpaModel.getType()), jpaModel.getKcal(), jpaModel.getFoodPackage().getId());
  }
}
