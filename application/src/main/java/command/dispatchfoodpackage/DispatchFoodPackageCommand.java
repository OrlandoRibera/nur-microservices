package command.dispatchfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;

public class DispatchFoodPackageCommand implements Command<FoodPackageDTO> {
  FoodPackageDTO foodPackageDTO;

  public DispatchFoodPackageCommand(FoodPackageDTO foodPackageDTO) {
    this.foodPackageDTO = foodPackageDTO;
  }
}
