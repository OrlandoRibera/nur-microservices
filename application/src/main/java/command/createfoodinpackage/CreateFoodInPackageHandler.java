package command.createfoodinpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import factories.foodpackage.CreateFoodPackage;
import factories.foodpackage.FoodPackageFactory;
import infrastructure.model.Food;
import infrastructure.repositories.FoodRepository;
import mappers.FoodMapper;
import mappers.FoodPackageMapper;
import infrastructure.model.FoodPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import infrastructure.repositories.FoodPackageRepository;

import java.util.ArrayList;
import java.util.Arrays;
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
      if (foodPackage == null) return null;

      Food foodToAdd = FoodMapper.from(request.foodDTO);
      foodRepository.create(foodToAdd);

      List<Food> newFoods = new ArrayList<>(foodPackage.getFoods());
      newFoods.add(foodToAdd);

      foodPackage = foodPackageFactory.create(
        foodPackage.getId(),
        foodPackage.getRecipeId(),
        foodPackage.getClientId(),
        foodPackage.getAddressId(),
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
