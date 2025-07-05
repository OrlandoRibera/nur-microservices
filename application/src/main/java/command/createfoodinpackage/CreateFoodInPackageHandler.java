package command.createfoodinpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import factories.foodpackage.CreateFoodPackage;
import factories.foodpackage.FoodPackageFactory;
import infrastructure.model.CustomException;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import mappers.FoodMapper;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CreateFoodInPackageHandler implements Command.Handler<CreateFoodInPackageCommand, FoodPackageDTO> {
  private final FoodPackageFactory foodPackageFactory;

  @Autowired
  private FoodPackageRepository foodPackageRepository;
  @Autowired
  private FoodRepository foodRepository;

  public CreateFoodInPackageHandler() {
    this.foodPackageFactory = new CreateFoodPackage();
  }

  @Override
  public FoodPackageDTO handle(CreateFoodInPackageCommand request) {
    try {
      FoodPackage foodPackage = foodPackageRepository.get(UUID.fromString(request.foodDTO.foodPackageId()));
      if (foodPackage == null) throw new CustomException("Food package not found");

      if (foodPackage.getStatus() != FoodPackageStatus.NEW && foodPackage.getStatus() != FoodPackageStatus.COOKING) {
        throw new CustomException("Cannot add more food because food package is in " + foodPackage.getStatus().toString() + " status");
      }

      Food foodToAdd = FoodMapper.from(request.foodDTO);
      foodRepository.create(foodToAdd);

      List<Food> newFoods = new ArrayList<>(foodPackage.getFoods());
      newFoods.add(foodToAdd);

      foodPackage = foodPackageFactory.create(
        foodPackage.getId(),
        foodPackage.getRecipeId(),
        foodPackage.getClientId(),
        foodPackage.getAddress(),
        newFoods,
        foodPackage.getStatus()
      );

      foodPackageRepository.update(foodPackage);
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
