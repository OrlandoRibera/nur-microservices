package infrastructure.utils;

import annotations.Generated;
import core.BusinessRuleValidationException;
import infrastructure.model.*;

import java.util.List;
import java.util.Objects;

@Generated
public class FoodPackageUtils {
	private FoodPackageUtils() {
	}

	public static FoodPackage jpaModelToFoodPackage(FoodPackageJpaModel jpaModel) {
		List<Food> foods = jpaModel.getFoods().stream()
			.map(food -> {
				try {
					return FoodUtils.jpaToFood(food);
				} catch (Exception e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.toList();

		return new FoodPackage(jpaModel.getId(), jpaModel.getRecipeId(), jpaModel.getClientId(), jpaModel.getAddressId(), foods, FoodPackageStatus.valueOf(jpaModel.getStatus()));
	}

	public static FoodPackageJpaModel foodPackageToJpaEntity(
		FoodPackage foodPackage,
		List<FoodJpaModel> foods
	) {
		FoodPackageJpaModel model = new FoodPackageJpaModel();
		model.setFoods(foods);
		model.setId(foodPackage.getId());
		model.setRecipeId(foodPackage.getRecipeId());
		model.setClientId(foodPackage.getClientId());
		model.setAddressId(foodPackage.getAddressId());
		return model;
	}
}
