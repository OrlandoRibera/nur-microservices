package infrastructure.repositories.recipe;

import infrastructure.model.CustomException;
import infrastructure.model.Recipe;
import infrastructure.model.RecipeJpaModel;
import infrastructure.repositories.RecipeRepository;
import infrastructure.utils.RecipeUtils;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecipeJpaRepository implements RecipeRepository {
	private RecipeCrudRepository recipeCrudRepository;

	public RecipeJpaRepository(RecipeCrudRepository recipeCrudRepository) {
	}

	@Override
	public Recipe get(UUID id) {
		RecipeJpaModel recipeJpaModel = recipeCrudRepository.findById(id.toString()).orElse(null);
		if (recipeJpaModel == null) throw new CustomException("Recipe not found");

		return RecipeUtils.jpaMotelToRecipe(recipeJpaModel);
	}

	@Override
	public UUID create(Recipe recipe) {
		RecipeJpaModel recipeJpaModel = new RecipeJpaModel(
			recipe.getId(),
			recipe.getClientId(),
			recipe.getPlanDetails()
		);

		recipeCrudRepository.save(recipeJpaModel);
		return UUID.fromString(recipe.getId());
	}
}
