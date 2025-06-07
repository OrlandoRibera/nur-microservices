package infrastructure.utils;

import core.BusinessRuleValidationException;
import infrastructure.model.Recipe;
import infrastructure.model.RecipeJpaModel;

public class RecipeUtils {
	private RecipeUtils() {
	}

	public static Recipe jpaMotelToRecipe(RecipeJpaModel recipeJpaModel) {
		try {
			return new Recipe(recipeJpaModel.getId(), recipeJpaModel.getClientId(), recipeJpaModel.getPlanDetails());
		} catch (BusinessRuleValidationException e) {
			return null;
		}
	}
}
