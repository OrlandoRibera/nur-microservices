package command.createfoodpackage;

import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import publisher.DomainEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


class CreateFoodPackageHandlerTest {
	@Mock
	private FoodPackageRepository foodPackageRepository;

	@Mock
	private DomainEventPublisher publisher;

	@InjectMocks
	private CreateFoodPackageHandler handler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldCreateFoodPackageSuccessfully() {
		// Arrange
		String recipeId = UUID.randomUUID().toString();
		String clientId = UUID.randomUUID().toString();
		String addressId = UUID.randomUUID().toString();
		CreateFoodPackageCommand command = new CreateFoodPackageCommand(recipeId, clientId, addressId);
		doNothing().when(publisher).publish(anyList());

		// Act
		FoodPackageDTO result = handler.handle(command);

		FoodPackageDTO expectedDTO = new FoodPackageDTO(result.id(), recipeId, clientId, addressId, List.of(),
			FoodPackageStatus.NEW);

		// Assert
		assertNotNull(result);
		assertEquals(expectedDTO, result);
		verify(foodPackageRepository).create(any(FoodPackage.class));
	}
}
