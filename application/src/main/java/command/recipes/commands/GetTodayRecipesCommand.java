package command.recipes.commands;

import an.awesome.pipelinr.Command;
import dto.recipes.RecipeDTO;

import java.util.List;

public class GetTodayRecipesCommand implements Command<List<RecipeDTO>> {
	public GetTodayRecipesCommand() {
	}
}
