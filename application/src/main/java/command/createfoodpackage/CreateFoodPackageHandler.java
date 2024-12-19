package command.createfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;
import factories.foodpackage.CreateFoodPackage;
import factories.foodpackage.FoodPackageFactory;
import mappers.FoodPackageMapper;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import infrastructure.repositories.FoodPackageRepository;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class CreateFoodPackageHandler implements Command.Handler<CreateFoodPackageCommand, FoodPackageDTO> {
  private final FoodPackageFactory foodPackageFactory;

  @Autowired
  private FoodPackageRepository foodPackageRepository;

  public CreateFoodPackageHandler() {
    this.foodPackageFactory = new CreateFoodPackage();
  }

  @Override
  public FoodPackageDTO handle(CreateFoodPackageCommand request) {
    FoodPackage foodPackage = foodPackageFactory.create(
      UUID.fromString(request.recipeId),
      UUID.fromString(request.clientId),
      UUID.fromString(request.addressId),
      new ArrayList<>(),
      FoodPackageStatus.EMPTY
    );

    foodPackageRepository.create(foodPackage);
    return FoodPackageMapper.from(foodPackage);
  }
}
