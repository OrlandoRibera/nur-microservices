package command.createfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;

public class CreateFoodPackageCommand implements Command<FoodPackageDTO> {
  String recipeId;
  String clientId;
  String addressId;

  public CreateFoodPackageCommand(String recipeId, String clientId, String addressId) {
    this.recipeId = recipeId;
    this.clientId = clientId;
    this.addressId = addressId;
  }
}
