package infrastructure.repositories.foodpackage;

import core.BusinessRuleValidationException;
import infrastructure.model.FoodJpaModel;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageJpaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.utils.FoodPackageUtils;
import infrastructure.utils.FoodUtils;

import java.util.List;
import java.util.UUID;

@Repository
public class FoodPackageJpaRepository implements FoodPackageRepository {
  @Autowired
  private FoodPackageCrudRepository foodPackageCrudRepository;

  @Override
  public UUID update(FoodPackage foodPackage) {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findById(foodPackage.getId()).orElse(null);
    if (foodPackageJpaModel == null) return null;

    List<FoodJpaModel> foods = FoodUtils.foodToJpaEntities(foodPackage.getFoods(), foodPackageJpaModel);
    return FoodPackageUtils.foodPackageToJpaEntity(foodPackage, foods).getId();
  }

  @Override
  public FoodPackage get(UUID id) throws BusinessRuleValidationException {

    FoodPackageJpaModel foodPackage = foodPackageCrudRepository.findById(id).orElse(null);
    if (foodPackage == null) return null;

    return FoodPackageUtils.jpaModelToFoodPackage(foodPackage);
  }

  @Override
  public FoodPackage findByRecipeIdAndClientId(UUID recipeId, UUID clientId) throws BusinessRuleValidationException {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findByRecipeIdAndClientId(recipeId, clientId);
    if (foodPackageJpaModel == null) return null;

    return FoodPackageUtils.jpaModelToFoodPackage(foodPackageJpaModel);
  }
}
