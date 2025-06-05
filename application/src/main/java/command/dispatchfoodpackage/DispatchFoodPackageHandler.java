package command.dispatchfoodpackage;

import an.awesome.pipelinr.Command;
import core.BusinessRuleValidationException;
import core.DomainEvent;
import dto.FoodPackageDTO;
import infrastructure.model.CustomException;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import mappers.FoodPackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.List;
import java.util.UUID;

@Component
public class DispatchFoodPackageHandler implements Command.Handler<DispatchFoodPackageCommand, FoodPackageDTO> {
	@Autowired
	private FoodPackageRepository foodPackageRepository;

	@Autowired
	private DomainEventPublisher publisher;


	@Override
	public FoodPackageDTO handle(DispatchFoodPackageCommand request) {
		try {
			FoodPackage foodPackage = foodPackageRepository.get(UUID.fromString(request.foodPackageId));
			if (foodPackage == null) throw new CustomException("Food package not found");

			foodPackage.nextStatus(FoodPackageStatus.DISPATCHED);

			List<DomainEvent> events = foodPackage.getDomainEvents();
			publisher.publish(events);

			foodPackageRepository.update(foodPackage);
			return FoodPackageMapper.from(foodPackage);
		} catch (BusinessRuleValidationException e) {
			return null;
		}
	}
}
