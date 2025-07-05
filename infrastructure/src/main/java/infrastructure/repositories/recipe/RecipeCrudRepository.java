package infrastructure.repositories.recipe;

import infrastructure.model.RecipeJpaModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecipeCrudRepository extends CrudRepository<RecipeJpaModel, String> {
	@Query("SELECT r FROM RecipeJpaModel r WHERE r.initDate <= :date AND r.endDate >= :date")
	List<RecipeJpaModel> findActiveRecipesOnDate(@Param("date") Date date);
}
