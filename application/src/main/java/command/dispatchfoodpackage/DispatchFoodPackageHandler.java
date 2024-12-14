package command.dispatchfoodpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import mappers.FoodPackageMapper;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import org.springframework.stereotype.Component;
import infrastructure.repositories.FoodPackageRepository;

import java.util.UUID;

@Component
public class DispatchFoodPackageHandler implements Command.Handler<DispatchFoodPackageCommand, FoodPackageDTO> {
  private final FoodPackageRepository foodPackageRepository;

  public DispatchFoodPackageHandler(FoodPackageRepository foodPackageRepository) {
    this.foodPackageRepository = foodPackageRepository;
  }

  @Override
  public FoodPackageDTO handle(DispatchFoodPackageCommand request) {
    try {
      FoodPackage foodPackage = null;
      foodPackage = foodPackageRepository.get(UUID.fromString(request.foodPackageDTO.id()));
      foodPackage.nextStatus(FoodPackageStatus.DISPATCHED);
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
