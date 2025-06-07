package command.changefoodstatus;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import core.DomainEvent;
import dto.FoodDTO;
import factories.food.CreateFood;
import factories.food.FoodFactory;
import infrastructure.model.*;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import mappers.FoodMapper;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.List;
import java.util.UUID;

@Component
public class ChangeFoodStatusHandler implements Command.Handler<ChangeFoodStatusCommand, FoodDTO> {
	private final FoodFactory foodFactory;
	private FoodPackageRepository foodPackageRepository;
	private FoodRepository foodRepository;
	private DomainEventPublisher publisher;

	public ChangeFoodStatusHandler(FoodPackageRepository foodPackageRepository, FoodRepository foodRepository, DomainEventPublisher publisher) {
		this.foodFactory = new CreateFood();
		this.foodPackageRepository = foodPackageRepository;
		this.foodRepository = foodRepository;
		this.publisher = publisher;
	}

	@Override
	public FoodDTO handle(ChangeFoodStatusCommand request) {
		try {
			Food food = foodRepository.get(UUID.fromString(request.changeFoodStatusDTO.foodId()));
			if (food == null) throw new CustomException("Food not found");

			FoodStatus newStatus = FoodStatus.valueOf(request.changeFoodStatusDTO.newStatus());
			food.nextStatus(newStatus);

			Food foodUpdated = foodFactory.create(
				food.getId(),
				food.getName(),
				food.getType(),
				food.getStatus(),
				food.getKcal(),
				food.getFoodPackageId()
			);

			foodRepository.update(foodUpdated);
			List<Food> foods = foodRepository.findByFoodPackageId(food.getFoodPackageId());
			int foodsNotCooked = foods.stream().filter(food1 -> food1.getStatus() != FoodStatus.COOKED).toList().size();

			FoodPackage foodPackage = foodPackageRepository.get(food.getFoodPackageId());

			// if foodpackage is empty, move it to cooking
			if (foodPackage.getStatus() == FoodPackageStatus.NEW) {
				foodPackage.nextStatus(FoodPackageStatus.COOKING);
			}

			// Verify if all the foods are cooked
			if (foodsNotCooked == 0) {
				foodPackage.nextStatus(FoodPackageStatus.COOKED);
			}

			List<DomainEvent> events = foodPackage.getDomainEvents();
			if (events.isEmpty()) {
				publisher.publish(events);
			}
			foodPackageRepository.update(foodPackage);

			return FoodMapper.from(foodUpdated);
		} catch (BusinessRuleValidationException e) {
			return null;
		}
	}
}
