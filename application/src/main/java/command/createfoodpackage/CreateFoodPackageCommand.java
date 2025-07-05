package command.createfoodpackage;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;

public class CreateFoodPackageCommand implements Command<FoodPackageDTO> {
	String recipeId;
	String clientId;

	public CreateFoodPackageCommand(String recipeId, String clientId) {
		this.recipeId = recipeId;
		this.clientId = clientId;
	}
}
