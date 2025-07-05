package command.changefoodstatus;

import core.BusinessRuleValidationException;
import dto.ChangeFoodStatusDTO;
import dto.FoodDTO;
import infrastructure.model.*;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import publisher.DomainEventPublisher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeFoodStatusHandlerTest {
	@Mock
	private FoodPackageRepository foodPackageRepository;

	@Mock
	private DomainEventPublisher publisher;

	@InjectMocks
	private ChangeFoodStatusHandler handler;

	@Mock
	private FoodRepository foodRepository;

	private static UUID foodId;
	private static UUID foodPackageId;
	private static ChangeFoodStatusCommand command;
	private static FoodPackage foodPackage;
	private static ChangeFoodStatusDTO dto;

	@BeforeAll
	static void setUpAll() throws BusinessRuleValidationException {
		foodId = UUID.randomUUID();
		foodPackageId = UUID.randomUUID();

		dto = new ChangeFoodStatusDTO(foodId.toString(), FoodStatus.COOKED.toString());
		command = new ChangeFoodStatusCommand(dto);
	}

	@BeforeEach
	void setUp() throws BusinessRuleValidationException {
		// Reset status
		Food food = new Food(foodId, "Pizza", FoodType.DINNER, FoodStatus.COOKING, 350.0f, foodPackageId);
		foodPackage = new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), "", List.of(food), FoodPackageStatus.COOKING);


		// Mock response from foodRepository
		when(foodRepository.get(foodId)).thenReturn(food);
		when(foodRepository.findByFoodPackageId(foodPackageId)).thenReturn(List.of(food));

		// Mock response from FoodPackageRepository
		when(foodPackageRepository.get(foodPackageId)).thenReturn(foodPackage);
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	void testHandleSuccess() throws BusinessRuleValidationException {
		doNothing().when(publisher).publish(anyList());

		FoodDTO result = handler.handle(command);

		// Result should not be null and Status should be Cooked
		assertNotNull(result);
		assertEquals(FoodStatus.COOKED.toString(), result.status());

		// All methods should have been called
		verify(foodRepository).get(foodId);
		verify(foodRepository).update(any(Food.class));
		verify(foodPackageRepository).update(any(FoodPackage.class));
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	void testHandleFoodNotFound() throws BusinessRuleValidationException {
		UUID foodIdNotFound = UUID.randomUUID();

		ChangeFoodStatusDTO dto = new ChangeFoodStatusDTO(foodIdNotFound.toString(), FoodStatus.COOKED.toString());
		ChangeFoodStatusCommand commandForNotFound = new ChangeFoodStatusCommand(dto);

		// Food is not found
		when(foodRepository.get(foodIdNotFound)).thenReturn(null);

		// Run command
		CustomException exception = assertThrows(CustomException.class, () -> handler.handle(commandForNotFound));

		// Message for null food
		assertEquals("Food not found", exception.getMessage());
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	void testHandleBusinessRuleValidationException() throws BusinessRuleValidationException {
		// Mock Food class
		Food foodMock = mock(Food.class);

		// Emulate get food from repository
		when(foodRepository.get(UUID.fromString(dto.foodId()))).thenReturn(foodMock);

		// Emulate throw
		doThrow(new BusinessRuleValidationException("Invalid status")).when(foodMock).nextStatus(any(FoodStatus.class));

		// Run handler
		FoodDTO result = handler.handle(command);

		// FoodDTO should be null
		assertNull(result);
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	void testHandleFoodPackageTransitionToCooking() throws BusinessRuleValidationException {
		// Setup FoodPackage on New status
		UUID uuid1 = UUID.randomUUID();
		Food food1 = new Food("Palta", FoodType.BREAKFAST, 100.0f, uuid1);
		Food food2 = new Food("Manga verde con sal", FoodType.BREAKFAST, 101.0f, uuid1);
		UUID food1Id = food1.getId();
		FoodPackage foodPackage1 = new FoodPackage(uuid1, UUID.randomUUID(), UUID.randomUUID(), "", List.of(food1, food2), FoodPackageStatus.NEW);

		when(foodPackageRepository.get(uuid1)).thenReturn(foodPackage1);
		when(foodRepository.get(food1Id)).thenReturn(food1);
		when(foodRepository.findByFoodPackageId(uuid1)).thenReturn((List.of(food1, food2)));

		// Run handler
		ChangeFoodStatusDTO dto1 = new ChangeFoodStatusDTO(food1Id.toString(), FoodStatus.COOKING.toString());
		ChangeFoodStatusCommand command1 = new ChangeFoodStatusCommand(dto1);
		FoodDTO result1 = handler.handle(command1);

		// Verify Foodpackage status is cooked because all foods where cooked
		assertEquals(FoodPackageStatus.COOKING, foodPackage1.getStatus());
	}

	@Test
	@MockitoSettings(strictness = Strictness.LENIENT)
	void testHandleFoodPackageNotEmptyOrNotAllCooked() throws BusinessRuleValidationException {
		Food cookedFood = new Food(foodId, "Pizza", FoodType.DINNER, FoodStatus.COOKED, 301.0f, foodPackageId);
		Food uncookedFood = new Food(foodId, "Pizza", FoodType.DINNER, FoodStatus.PENDING, 302.0f, foodPackageId);

		when(foodRepository.findByFoodPackageId(foodPackageId)).thenReturn(List.of(cookedFood, uncookedFood));
		when(foodPackageRepository.get(foodPackageId)).thenReturn(foodPackage);

		assertEquals(FoodPackageStatus.COOKING, foodPackage.getStatus());
	}
}
