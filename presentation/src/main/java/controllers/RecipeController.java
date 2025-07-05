package controllers;


import an.awesome.pipelinr.Pipeline;
import command.recipes.commands.GetTodayRecipesCommand;
import dto.recipes.RecipeDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
	final Pipeline pipeline;

	public RecipeController(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	@GetMapping()
	public List<RecipeDTO> getAllRecipes() {
		GetTodayRecipesCommand getTodayRecipesCommand = new GetTodayRecipesCommand();
		return getTodayRecipesCommand.execute(pipeline);
	}
}
