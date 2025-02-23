package command.packfoodpackage;

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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PackFoodPackageHandlerTest {
  @Mock
  private FoodPackageRepository foodPackageRepository;

  @InjectMocks
  private PackFoodPackageHandler handler;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldThordFoodPackageNotFound() throws BusinessRuleValidationException {
    UUID foodPackageId = UUID.randomUUID();
    PackFoodPackageCommand command = new PackFoodPackageCommand(foodPackageId.toString());

    when(foodPackageRepository.get(foodPackageId)).thenReturn(null);

    CustomException exception = assertThrows(CustomException.class, () -> handler.handle(command));
    assertEquals("Food package not found", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionInvalidStatus() throws BusinessRuleValidationException {
    String foodPackageId = UUID.randomUUID().toString();
    PackFoodPackageCommand command = new PackFoodPackageCommand(foodPackageId);
    FoodPackage foodPackage = new FoodPackage(UUID.fromString(foodPackageId), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.NEW);
    when(foodPackageRepository.get(UUID.fromString(foodPackageId))).thenReturn(foodPackage);

    // FoodDTO should be null
    assertNull(handler.handle(command));
  }

  @Test
  void shouldPackFoodPackageSuccessfully() throws BusinessRuleValidationException {
    String foodPackageId = UUID.randomUUID().toString();
    PackFoodPackageCommand command = new PackFoodPackageCommand(foodPackageId);
    FoodPackage foodPackage = new FoodPackage(UUID.fromString(foodPackageId), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), List.of(), FoodPackageStatus.COOKED);
    when(foodPackageRepository.get(UUID.fromString(foodPackageId))).thenReturn(foodPackage);

    FoodPackageDTO result = handler.handle(command);

    assertNotNull(result);
    assertEquals(result.getClass(), FoodPackageDTO.class);
    assertEquals(result.status(), FoodPackageStatus.PACKED);
  }
}