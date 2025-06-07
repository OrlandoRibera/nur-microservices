package infrastructure.repositories;

import infrastructure.model.Recipe;

import java.util.UUID;

public interface RecipeRepository {
	Recipe get(UUID id);

	UUID create(Recipe recipe);
}
