package command.dispatchfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;
import dto.PackDispatchFoodPackageDTO;

public class DispatchFoodPackageCommand implements Command<FoodPackageDTO> {
  String foodPackageId;

  public DispatchFoodPackageCommand(String foodPackageId) {
    this.foodPackageId = foodPackageId;
  }
}
