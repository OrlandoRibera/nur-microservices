package command.createfoodinpackage;

import an.awesome.pipelinr.Command;
import dto.FoodDTO;
import dto.FoodPackageDTO;

public class CreateFoodInPackageCommand implements Command<FoodPackageDTO> {
  FoodDTO foodDTO;

  public CreateFoodInPackageCommand(FoodDTO foodDTO) {
    this.foodDTO = foodDTO;
  }
}
