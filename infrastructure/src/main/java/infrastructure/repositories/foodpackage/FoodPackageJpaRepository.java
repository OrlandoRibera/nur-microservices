package infrastructure.repositories.foodpackage;

import core.BusinessRuleValidationException;
import infrastructure.model.CustomException;
import infrastructure.model.FoodJpaModel;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageJpaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.utils.FoodPackageUtils;
import infrastructure.utils.FoodUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class FoodPackageJpaRepository implements FoodPackageRepository {
  @Autowired
  private FoodPackageCrudRepository foodPackageCrudRepository;


  @Override
  public UUID create(FoodPackage foodPackage) {
    FoodPackageJpaModel jpaModel = new FoodPackageJpaModel();
    List<FoodJpaModel> foods = FoodUtils.foodToJpaEntities(foodPackage.getFoods(), jpaModel);

    jpaModel.setId(foodPackage.getId());
    jpaModel.setStatus(foodPackage.getStatus().name());
    jpaModel.setRecipeId(foodPackage.getRecipeId());
    jpaModel.setClientId(foodPackage.getClientId());
    jpaModel.setAddressId(foodPackage.getAddressId());
    jpaModel.setFoods(foods);

    return foodPackageCrudRepository.save(jpaModel).getId();
  }

  @Override
  public UUID update(FoodPackage foodPackage) {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findById(foodPackage.getId()).orElse(null);
    if (foodPackageJpaModel == null) throw new CustomException("Food package not found");

    foodPackageJpaModel.getFoods().clear();

    List<FoodJpaModel> foods = FoodUtils.foodToJpaEntities(foodPackage.getFoods(), foodPackageJpaModel);
    foodPackageJpaModel.getFoods().addAll(foods);
    foodPackageJpaModel.setStatus(foodPackage.getStatus().toString());

    return foodPackageCrudRepository.save(foodPackageJpaModel).getId();
  }

  @Override
  public List<FoodPackage> getAll() throws BusinessRuleValidationException {
    List<FoodPackageJpaModel> foodPackageJpaModel = StreamSupport.stream(foodPackageCrudRepository.findAll().spliterator(), false).collect(Collectors.toList());

    List<FoodPackage> foodPackages = new ArrayList<>();
    for (FoodPackageJpaModel foodPackage : foodPackageJpaModel) {
      foodPackages.add(FoodPackageUtils.jpaModelToFoodPackage(foodPackage));
    }
    return foodPackages;
  }

  @Override
  public FoodPackage get(UUID id) throws BusinessRuleValidationException {

    FoodPackageJpaModel foodPackage = foodPackageCrudRepository.findById(id).orElse(null);
    if (foodPackage == null) throw new CustomException("Food package not found");

    return FoodPackageUtils.jpaModelToFoodPackage(foodPackage);
  }

  @Override
  public FoodPackage findByRecipeIdAndClientId(UUID recipeId, UUID clientId) throws BusinessRuleValidationException {
    FoodPackageJpaModel foodPackageJpaModel = foodPackageCrudRepository.findByRecipeIdAndClientId(recipeId, clientId);
    if (foodPackageJpaModel == null) throw new CustomException("Food package not found");

    return FoodPackageUtils.jpaModelToFoodPackage(foodPackageJpaModel);
  }
}
