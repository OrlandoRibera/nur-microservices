package command.createfoodpackage;

import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import publisher.DomainEventPublisher;


class CreateFoodPackageHandlerTest {
	@Mock
	private FoodPackageRepository foodPackageRepository;
	@Mock
	private UserRepository userRepository;

	@Mock
	private DomainEventPublisher publisher;

	@InjectMocks
	private CreateFoodPackageHandler handler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
}
