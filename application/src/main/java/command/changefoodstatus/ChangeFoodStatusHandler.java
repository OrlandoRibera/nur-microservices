package command.changefoodstatus;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import factories.food.CreateFood;
import factories.food.FoodFactory;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodStatus;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChangeFoodStatusHandler implements Command.Handler<ChangeFoodStatusCommand, FoodPackageDTO> {
  private final FoodFactory foodFactory;

  @Autowired
  private FoodPackageRepository foodPackageRepository;
  @Autowired
  private FoodRepository foodRepository;

  public ChangeFoodStatusHandler() {
    this.foodFactory = new CreateFood();
  }

  @Override
  public FoodPackageDTO handle(ChangeFoodStatusCommand request) {
    try {
      Food food = foodRepository.get(UUID.fromString(request.changeFoodStatusDTO.foodId()));
      if (food == null) return null;

      Food newFood = foodFactory.create(
        food.getId(),
        food.getName(),
        food.getType(),
        FoodStatus.valueOf(request.changeFoodStatusDTO.newStatus()),
        food.getKcal(),
        food.getFoodPackageId()
      );

      foodRepository.update(newFood);
      FoodPackage foodPackage = foodPackageRepository.get(food.getFoodPackageId());
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
