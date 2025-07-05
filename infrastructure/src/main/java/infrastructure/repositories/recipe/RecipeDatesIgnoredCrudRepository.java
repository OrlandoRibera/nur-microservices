package infrastructure.repositories.recipe;

import infrastructure.model.RecipeDatesIgnoredJpaModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RecipeDatesIgnoredCrudRepository extends CrudRepository<RecipeDatesIgnoredJpaModel, String> {
	@Query("SELECT r FROM RecipeDatesIgnoredJpaModel r WHERE r.recipeId = :recipeId AND r.fromDate <= :date AND r.toDate >= :date")
	List<RecipeDatesIgnoredJpaModel> findByDateAndRecipeId(@Param("date") Date date, @Param("recipeId") String recipeId);
}
