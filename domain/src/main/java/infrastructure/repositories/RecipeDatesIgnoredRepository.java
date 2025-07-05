package infrastructure.repositories;

import infrastructure.model.RecipeDatesIgnored;

import java.util.Date;
import java.util.List;

public interface RecipeDatesIgnoredRepository {
	RecipeDatesIgnored create(RecipeDatesIgnored recipeDatesIgnored);

	List<RecipeDatesIgnored> getByDateAndRecipeId(Date date, String recipeId);
}
