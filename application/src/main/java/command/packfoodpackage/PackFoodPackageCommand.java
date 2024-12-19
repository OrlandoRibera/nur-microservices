package command.packfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;

public class PackFoodPackageCommand implements Command<FoodPackageDTO> {
  String foodPackageId;

  public PackFoodPackageCommand(String foodPackageId) {
    this.foodPackageId = foodPackageId;
  }
}
