package infrastructure.repositories;

import infrastructure.model.Recipe;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RecipeRepository {
	Recipe get(UUID id);

	UUID create(Recipe recipe);

	List<Recipe> getByDate(Date date);
}
