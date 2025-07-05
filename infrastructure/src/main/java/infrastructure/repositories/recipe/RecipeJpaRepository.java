package infrastructure.repositories.recipe;

import infrastructure.model.CustomException;
import infrastructure.model.Recipe;
import infrastructure.model.RecipeJpaModel;
import infrastructure.repositories.RecipeRepository;
import infrastructure.utils.RecipeUtils;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class RecipeJpaRepository implements RecipeRepository {
	private RecipeCrudRepository recipeCrudRepository;

	public RecipeJpaRepository(RecipeCrudRepository recipeCrudRepository) {
		this.recipeCrudRepository = recipeCrudRepository;
	}

	@Override
	public Recipe get(UUID id) {
		RecipeJpaModel recipeJpaModel = recipeCrudRepository.findById(id.toString()).orElse(null);
		if (recipeJpaModel == null) throw new CustomException("Recipe not found");

		return RecipeUtils.jpaMotelToRecipe(recipeJpaModel);
	}

	@Override
	public UUID create(Recipe recipe) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, 30);
		Date endDate = calendar.getTime();

		RecipeJpaModel recipeJpaModel = new RecipeJpaModel(recipe.getId(), recipe.getClientId(), recipe.getPlanDetails(), now, endDate);

		recipeCrudRepository.save(recipeJpaModel);
		return UUID.fromString(recipe.getId());
	}

	@Override
	public List<Recipe> getByDate(Date date) {
		List<RecipeJpaModel> jpaModels = recipeCrudRepository.findActiveRecipesOnDate(date);

		return jpaModels.stream().map(RecipeUtils::jpaMotelToRecipe).toList();
	}

}
