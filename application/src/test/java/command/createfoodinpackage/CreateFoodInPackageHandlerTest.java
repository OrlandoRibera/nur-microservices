package command.createfoodinpackage;

import core.BusinessRuleValidationException;
import dto.FoodDTO;
import dto.FoodPackageDTO;
import factories.foodpackage.FoodPackageFactory;
import infrastructure.model.*;
import infrastructure.repositories.FoodPackageRepository;
import infrastructure.repositories.FoodRepository;
import mappers.FoodPackageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateFoodInPackageHandlerTest {
  @Mock
  private FoodPackageRepository foodPackageRepository;
  @Mock
  private FoodRepository foodRepository;
  @Mock
  private FoodPackageFactory foodPackageFactory;

  @InjectMocks
  private CreateFoodInPackageHandler handler;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldThrowExceptionWhenFoodPackageNotFound() throws BusinessRuleValidationException {
    // Arrange
    CreateFoodInPackageCommand command = new CreateFoodInPackageCommand(new FoodDTO("non-existing-id", "Palta ew", "PENDING", "BREAKFAST", 100.0f, UUID.randomUUID().toString()));
    when(foodPackageRepository.get(any(UUID.class))).thenReturn(null);

    // Act and Assert
    CustomException exception = assertThrows(CustomException.class, () -> handler.handle(command));
    assertEquals("Food package not found", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenFoodPackageInInvalidStatus() throws BusinessRuleValidationException {
    // Arrange
    CreateFoodInPackageCommand command = new CreateFoodInPackageCommand(new FoodDTO("non-existing-id", "Palta ew", "PENDING", "BREAKFAST", 100.0f, UUID.randomUUID().toString()));
    FoodPackage foodPackage = new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.DISPATCHED);
    when(foodPackageRepository.get(any(UUID.class))).thenReturn(foodPackage);

    // Act and Assert
    CustomException exception = assertThrows(CustomException.class, () -> handler.handle(command));
    assertEquals("Cannot add more food because food package is in DISPATCHED status", exception.getMessage());
  }

  @Test
  void shouldCreateFoodInFoodPackage() throws BusinessRuleValidationException {
    // Arrange
    FoodPackage foodPackage = new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.NEW);
    when(foodPackageRepository.get(foodPackage.getId())).thenReturn(foodPackage);

    String foodDTOId = UUID.randomUUID().toString();
    CreateFoodInPackageCommand command = new CreateFoodInPackageCommand(new FoodDTO(foodDTOId, "Palta ew", "PENDING", "BREAKFAST", 100.0f, foodPackage.getId().toString()));

    // Act
    FoodPackageDTO result = handler.handle(command);

    // Assert
    assertNotNull(result);
    assertEquals(result.getClass(), FoodPackageDTO.class);
    assertEquals(result.foods().size(), 1);
    assertEquals(result.status(), FoodPackageStatus.NEW);
  }

  @Test
  void shouldReturnNullWhenBusinessRuleValidationExceptionOccurs() throws BusinessRuleValidationException {
    // Arrange
    CreateFoodInPackageCommand command = new CreateFoodInPackageCommand(new FoodDTO(UUID.randomUUID().toString(),
      "Papas", "PENDING", "BREAKFAST", 100.0f, UUID.randomUUID().toString()));

    FoodPackage foodPackage = new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.NEW);
    doThrow(new BusinessRuleValidationException("Food package not found")).when(foodPackageRepository).get(any(UUID.class));

    // Act
    FoodPackageDTO result = handler.handle(command);

    // Assert
    assertNull(result);
  }
}