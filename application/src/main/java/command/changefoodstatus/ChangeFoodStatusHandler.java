package command.changefoodstatus;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodDTO;
import factories.food.CreateFood;
import factories.food.FoodFactory;
import infrastructure.model.Food;
import infrastructure.model.FoodStatus;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import mappers.FoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChangeFoodStatusHandler implements Command.Handler<ChangeFoodStatusCommand, FoodDTO> {
  private final FoodFactory foodFactory;

  @Autowired
  private FoodPackageRepository foodPackageRepository;
  @Autowired
  private FoodRepository foodRepository;

  public ChangeFoodStatusHandler() {
    this.foodFactory = new CreateFood();
  }

  @Override
  public FoodDTO handle(ChangeFoodStatusCommand request) {
    try {
      Food food = foodRepository.get(UUID.fromString(request.changeFoodStatusDTO.foodId()));
      if (food == null) return null;

      FoodStatus newStatus = FoodStatus.valueOf(request.changeFoodStatusDTO.newStatus());

      Food foodUpdated = foodFactory.create(
        food.getId(),
        food.getName(),
        food.getType(),
        newStatus,
        food.getKcal(),
        food.getFoodPackageId()
      );

      foodRepository.update(foodUpdated);
      return FoodMapper.from(foodUpdated);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
