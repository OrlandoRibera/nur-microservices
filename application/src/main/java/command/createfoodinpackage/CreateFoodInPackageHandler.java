package command.createfoodinpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import factories.foodpackage.CreateFoodPackage;
import factories.foodpackage.FoodPackageFactory;
import mappers.FoodMapper;
import mappers.FoodPackageMapper;
import infrastructure.model.FoodPackage;
import org.springframework.stereotype.Component;
import infrastructure.repositories.FoodPackageRepository;

import java.util.UUID;

@Component
public class CreateFoodInPackageHandler implements Command.Handler<CreateFoodInPackageCommand, FoodPackageDTO> {
  private final FoodPackageFactory foodPackageFactory;
  private final FoodPackageRepository foodPackageRepository;

  public CreateFoodInPackageHandler(FoodPackageRepository foodPackageRepository) {
    this.foodPackageFactory = new CreateFoodPackage();
    this.foodPackageRepository = foodPackageRepository;
  }

  @Override
  public FoodPackageDTO handle(CreateFoodInPackageCommand request) {
    try {
      FoodPackage foodPackage = foodPackageRepository.get(UUID.fromString(request.foodDTO.foodPackageId()));
      foodPackage.getFoods().add(FoodMapper.from(request.foodDTO));

      foodPackage = foodPackageFactory.create(
        foodPackage.getId(),
        foodPackage.getRecipeId(),
        foodPackage.getClientId(),
        foodPackage.getAddressId(),
        foodPackage.getFoods(),
        foodPackage.getStatus()
      );

      foodPackageRepository.update(foodPackage);
      return FoodPackageMapper.from(foodPackage);
    } catch (BusinessRuleValidationException e) {
      return null;
    }
  }
}
