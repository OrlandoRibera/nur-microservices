package infrastructure.repositories.recipe;

import infrastructure.model.RecipeJpaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCrudRepository extends CrudRepository<RecipeJpaModel, String> {
}
