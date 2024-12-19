package command.dispatchfoodpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DispatchFoodPackageHandler implements Command.Handler<DispatchFoodPackageCommand, FoodPackageDTO> {
  @Autowired
  private FoodPackageRepository foodPackageRepository;

  @Override
  public FoodPackageDTO handle(DispatchFoodPackageCommand request) {
    try {
      FoodPackage foodPackage = foodPackageRepository.get(UUID.fromString(request.foodPackageDTO.id()));
      foodPackage.nextStatus(FoodPackageStatus.DISPATCHED);

      foodPackageRepository.update(foodPackage);
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
