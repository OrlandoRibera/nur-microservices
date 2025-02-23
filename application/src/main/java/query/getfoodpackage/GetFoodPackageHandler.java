package query.getfoodpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import mappers.FoodPackageMapper;
import infrastructure.model.FoodPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import infrastructure.repositories.FoodPackageRepository;

import java.util.UUID;

@Component
public class GetFoodPackageHandler implements Command.Handler<GetFoodPackageQuery, FoodPackageDTO> {

  @Autowired
  private FoodPackageRepository foodPackageRepository;

  @Override
  public FoodPackageDTO handle(GetFoodPackageQuery request) {
    try {
      FoodPackage foodPackage;
      foodPackage = foodPackageRepository.findByRecipeIdAndClientId(UUID.fromString(request.recipeId), UUID.fromString(request.clientId));
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
