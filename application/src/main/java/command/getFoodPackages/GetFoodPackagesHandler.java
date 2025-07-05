package command.getFoodPackages;

import an.awesome.pipelinr.Command;
import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;
import infrastructure.repositories.FoodPackageRepository;
import mappers.FoodPackageMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetFoodPackagesHandler implements Command.Handler<GetFoodPackagesCommand, List<FoodPackageDTO>> {
	private FoodPackageRepository foodPackageRepository;

	public GetFoodPackagesHandler(FoodPackageRepository foodPackageRepository) {
		this.foodPackageRepository = foodPackageRepository;
	}

	@Override
	public List<FoodPackageDTO> handle(GetFoodPackagesCommand getFoodPackagesCommand) {
		List<FoodPackage> foodPackages;

		foodPackages = foodPackageRepository.getAll();

		List<FoodPackageDTO> foodPackageDTOS = new ArrayList<>();
		for (FoodPackage foodPackage : foodPackages) {
			foodPackageDTOS.add(FoodPackageMapper.from(foodPackage));
		}
		return foodPackageDTOS;
	}
}
