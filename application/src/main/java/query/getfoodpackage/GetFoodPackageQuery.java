package query.getfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;

public class GetFoodPackageQuery implements Command<FoodPackageDTO> {

  String recipeId;
  String clientId;

  public GetFoodPackageQuery(String recipeId, String clientId) {
    this.recipeId = recipeId;
    this.clientId = clientId;
  }
}
