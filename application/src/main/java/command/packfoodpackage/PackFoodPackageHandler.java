package command.packfoodpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import infrastructure.model.CustomException;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PackFoodPackageHandler implements Command.Handler<PackFoodPackageCommand, FoodPackageDTO> {
  @Autowired
  private FoodPackageRepository foodPackageRepository;

  @Override
  public FoodPackageDTO handle(PackFoodPackageCommand request) {
    try {
      FoodPackage foodPackage = foodPackageRepository.get(UUID.fromString(request.foodPackageId));
      if (foodPackage == null) throw new CustomException("Food package not found");

      foodPackage.nextStatus(FoodPackageStatus.PACKED);

      foodPackageRepository.update(foodPackage);
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
