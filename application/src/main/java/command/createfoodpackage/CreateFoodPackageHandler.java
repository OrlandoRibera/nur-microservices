package command.createfoodpackage;

import an.awesome.pipelinr.Command;
import core.DomainEvent;
import dto.FoodPackageDTO;
import factories.foodpackage.CreateFoodPackage;
import factories.foodpackage.FoodPackageFactory;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.UserRepository;
import mappers.FoodPackageMapper;
import org.springframework.stereotype.Component;
import publisher.DomainEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CreateFoodPackageHandler implements Command.Handler<CreateFoodPackageCommand, FoodPackageDTO> {
	private final FoodPackageFactory foodPackageFactory;

	private FoodPackageRepository foodPackageRepository;
	private UserRepository userRepository;
	private DomainEventPublisher publisher;

	public CreateFoodPackageHandler(FoodPackageRepository foodPackageRepository, UserRepository userRepository, DomainEventPublisher publisher) {
		this.foodPackageFactory = new CreateFoodPackage();

		this.userRepository = userRepository;
		this.foodPackageRepository = foodPackageRepository;
		this.publisher = publisher;
	}

	@Override
	public FoodPackageDTO handle(CreateFoodPackageCommand request) {
		String address = userRepository.get(UUID.fromString(request.clientId)).getAddress();
		FoodPackage foodPackage = foodPackageFactory.create(UUID.fromString(request.recipeId), UUID.fromString(request.clientId), address, new ArrayList<>(), FoodPackageStatus.NEW);

		List<DomainEvent> events = foodPackage.getDomainEvents();
		publisher.publish(events);

		foodPackageRepository.create(foodPackage);
		return FoodPackageMapper.from(foodPackage);
	}
}
