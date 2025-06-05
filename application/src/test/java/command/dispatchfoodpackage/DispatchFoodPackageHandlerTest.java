package command.dispatchfoodpackage;

import core.BusinessRuleValidationException;
import dto.FoodPackageDTO;
import infrastructure.model.CustomException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class DispatchFoodPackageHandlerTest {
	@Mock
	private FoodPackageRepository foodPackageRepository;

	@Mock
	private DomainEventPublisher publisher;

	@InjectMocks
	private DispatchFoodPackageHandler handler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldDispatchFoodPackageSuccessfully() throws BusinessRuleValidationException {
		String foodPackageId = UUID.randomUUID().toString();
		DispatchFoodPackageCommand command = new DispatchFoodPackageCommand(foodPackageId);
		FoodPackage foodPackage = new FoodPackage(UUID.fromString(foodPackageId), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.PACKED);

		when(foodPackageRepository.get(UUID.fromString(foodPackageId))).thenReturn(foodPackage);
		doNothing().when(publisher).publish(foodPackage.getDomainEvents());

		FoodPackageDTO result = handler.handle(command);

		assertEquals(result.addressId(), foodPackage.getAddressId().toString());
		assertEquals(result.clientId(), foodPackage.getClientId().toString());
		assertEquals(result.recipeId(), foodPackage.getRecipeId().toString());
		assertNotNull(result);
		assertEquals(result.getClass(), FoodPackageDTO.class);
		assertEquals(result.status(), FoodPackageStatus.DISPATCHED);
	}

	@Test
	void shouldThrowExceptionFoodPackageNotFound() throws BusinessRuleValidationException {
		String foodPackageId = UUID.randomUUID().toString();
		DispatchFoodPackageCommand command = new DispatchFoodPackageCommand(foodPackageId);

		when(foodPackageRepository.get(UUID.fromString(foodPackageId))).thenReturn(null);
		CustomException exception = assertThrows(CustomException.class, () -> handler.handle(command));
		assertEquals("Food package not found", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionInvalidStatus() throws BusinessRuleValidationException {
		String foodPackageId = UUID.randomUUID().toString();
		DispatchFoodPackageCommand command = new DispatchFoodPackageCommand(foodPackageId);
		FoodPackage foodPackage = new FoodPackage(UUID.fromString(foodPackageId), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.NEW);
		when(foodPackageRepository.get(UUID.fromString(foodPackageId))).thenReturn(foodPackage);

		// FoodDTO should be null
		assertNull(handler.handle(command));
	}
}
