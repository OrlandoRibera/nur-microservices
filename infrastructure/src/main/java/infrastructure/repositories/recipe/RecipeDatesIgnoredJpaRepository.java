package infrastructure.repositories.recipe;

import infrastructure.model.RecipeDatesIgnoredJpaModel;
import infrastructure.model.RecipeDatesIgnored;
import infrastructure.repositories.RecipeDatesIgnoredRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class RecipeDatesIgnoredJpaRepository implements RecipeDatesIgnoredRepository {
	private RecipeDatesIgnoredCrudRepository recipeDatesIgnoredCrudRepository;

	public RecipeDatesIgnoredJpaRepository(RecipeDatesIgnoredCrudRepository recipeDatesIgnoredCrudRepository) {
		this.recipeDatesIgnoredCrudRepository = recipeDatesIgnoredCrudRepository;
	}

	@Override
	public RecipeDatesIgnored create(RecipeDatesIgnored recipeDatesIgnored) {
		UUID id = UUID.randomUUID();
		RecipeDatesIgnoredJpaModel recipeDatesChangedJpaModel = recipeDatesIgnoredCrudRepository.save(new RecipeDatesIgnoredJpaModel(id.toString(), recipeDatesIgnored.getId().toString(), recipeDatesIgnored.getClientId().toString(), recipeDatesIgnored.getPreviousDate(), recipeDatesIgnored.getNewDate()));

		return new RecipeDatesIgnored(UUID.fromString(recipeDatesChangedJpaModel.getId()), UUID.fromString(recipeDatesChangedJpaModel.getClientId()), recipeDatesChangedJpaModel.getFromDate(), recipeDatesChangedJpaModel.getToDate());
	}

	@Override
	public List<RecipeDatesIgnored> getByDateAndRecipeId(Date date, String recipeId) {
		List<RecipeDatesIgnoredJpaModel> recipeDatesIgnoredList = recipeDatesIgnoredCrudRepository.findByDateAndRecipeId(date, recipeId);
		if (recipeDatesIgnoredList != null && !recipeDatesIgnoredList.isEmpty()) {
			return recipeDatesIgnoredList.stream().map(recipeDatesIgnoredJpaModel -> new RecipeDatesIgnored(UUID.fromString(recipeDatesIgnoredJpaModel.getId()), UUID.fromString(recipeDatesIgnoredJpaModel.getClientId()), recipeDatesIgnoredJpaModel.getFromDate(), recipeDatesIgnoredJpaModel.getToDate())).toList();
		}
		return List.of();
	}
}
