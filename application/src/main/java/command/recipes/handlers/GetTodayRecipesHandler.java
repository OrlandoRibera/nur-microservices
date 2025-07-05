package command.recipes.handlers;

import an.awesome.pipelinr.Command;
import command.recipes.commands.GetTodayRecipesCommand;
import dto.recipes.RecipeDTO;
import infrastructure.model.Recipe;
import infrastructure.model.RecipeDatesIgnored;
import infrastructure.model.User;
import infrastructure.repositories.RecipeDatesIgnoredRepository;
import infrastructure.repositories.RecipeRepository;
import infrastructure.repositories.UserRepository;
import mappers.recipes.RecipeMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GetTodayRecipesHandler implements Command.Handler<GetTodayRecipesCommand, List<RecipeDTO>> {

	private final RecipeRepository recipeRepository;
	private final UserRepository userRepository;
	private final RecipeDatesIgnoredRepository recipeDatesIgnoredRepository;

	public GetTodayRecipesHandler(RecipeRepository recipeRepository, UserRepository userRepository, RecipeDatesIgnoredRepository recipeDatesIgnoredRepository) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
		this.recipeDatesIgnoredRepository = recipeDatesIgnoredRepository;
	}

	@Override
	public List<RecipeDTO> handle(GetTodayRecipesCommand getTodayRecipesCommand) {
		// Get tomorrow
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, 1);
		Date tomorrow = c.getTime();

		// Get recipes where tomorrow Date is in the contract
		List<Recipe> recipes = recipeRepository.getByDate(tomorrow);
		List<RecipeDTO> recipeDTOList = new ArrayList<>();


		for (Recipe recipe : recipes) {
			User user = userRepository.get(UUID.fromString(recipe.getClientId()));

			// Check if user moves the date for deliver
			List<RecipeDatesIgnored> recipeDatesIgnored = recipeDatesIgnoredRepository.getByDateAndRecipeId(tomorrow, recipe.getId());
			if (!recipeDatesIgnored.isEmpty()) continue;

			String todayAddress = user.getAddress();
			recipeDTOList.add(RecipeMapper.from(recipe, user.getFullName(), todayAddress));
		}

		return recipeDTOList;
	}
}
