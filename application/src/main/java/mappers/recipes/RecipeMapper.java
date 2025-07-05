package mappers.recipes;

import core.BusinessRuleValidationException;
import dto.recipes.RecipeDTO;
import infrastructure.model.Recipe;

public final class RecipeMapper {
	private RecipeMapper() {
	}

	public static RecipeDTO from(Recipe recipe, String clientName, String clientAddress) {
		return new RecipeDTO(recipe.getId(), recipe.getPlanDetails(), recipe.getClientId(), clientName, clientAddress);
	}

	public static Recipe to(RecipeDTO recipeDTO) throws BusinessRuleValidationException {
		return new Recipe(recipeDTO.id(), recipeDTO.clientId(), recipeDTO.planDetail());
	}
}
