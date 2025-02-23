package command.getFoodPackages;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;
import infrastructure.repositories.FoodPackageRepository;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetFoodPackagesHandler implements Command.Handler<GetFoodPackagesCommand, List<FoodPackageDTO>> {
  @Autowired
  private FoodPackageRepository foodPackageRepository;

  @Override
  public List<FoodPackageDTO> handle(GetFoodPackagesCommand getFoodPackagesCommand) {
    List<FoodPackage> foodPackages;

    foodPackages = foodPackageRepository.getAll();

    List<FoodPackageDTO> foodPackageDTOS = new ArrayList<>();
    for (FoodPackage foodPackage : foodPackages) {
      foodPackageDTOS.add(FoodPackageMapper.from(foodPackage));
    }
    return foodPackageDTOS;
  }
}
