package infrastructure.model;

import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class FoodTest {

  @Test
  void invalidNameShouldThrowException() throws BusinessRuleValidationException {
    assertThrows(BusinessRuleValidationException.class, () -> {
      Food food = new Food(UUID.randomUUID(), null, FoodType.LUNCH, FoodStatus.PENDING, 500.0f, UUID.randomUUID());
    });
    assertThrows(BusinessRuleValidationException.class, () -> {
      Food food = new Food(UUID.randomUUID(), "", FoodType.LUNCH, FoodStatus.PENDING, 500.0f, UUID.randomUUID());
    });
    assertThrows(BusinessRuleValidationException.class, () -> {
      Food food = new Food(UUID.randomUUID(), null, FoodType.BREAKFAST, 500.0f, UUID.randomUUID());
    });
    assertThrows(BusinessRuleValidationException.class, () -> {
      Food food = new Food(UUID.randomUUID(), "", FoodType.BREAKFAST, 500.0f, UUID.randomUUID());
    });

  }

  @Test
  void validCreationOfFood() throws BusinessRuleValidationException {
    Food food = new Food("Panchito", FoodType.LUNCH, FoodStatus.COOKED, 300.0f, UUID.randomUUID());
    assertEquals(food.getStatus(), FoodStatus.COOKED);
  }

  @Test
  void validCreationOfFoodWithIdAndStatus() throws BusinessRuleValidationException {
    UUID id = UUID.randomUUID();
    Food food = new Food(id, "Panchito", FoodType.LUNCH, FoodStatus.COOKED, 300.0f, UUID.randomUUID());
    assertEquals(food.getId(), id);
  }

  @Test
  void nextStatusValidTransition() throws BusinessRuleValidationException {
    // Arrange
    UUID foodId = UUID.randomUUID();
    Food food = new Food(foodId, "Pizza", FoodType.BREAKFAST, 500.0f, UUID.randomUUID());

    // Act: Transition from PENDING to COOKING
    food.nextStatus(FoodStatus.COOKING);

    // Assert
    assertEquals(FoodStatus.COOKING, food.getStatus());
  }

  @Test
  void nextStatusInValidTransition() throws BusinessRuleValidationException {
    UUID foodId = UUID.randomUUID();
    Food food = new Food(foodId, "Pizza", FoodType.BREAKFAST, 500.0f, UUID.randomUUID());

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      food.nextStatus(FoodStatus.COOKED);
    });

    assertEquals("Invalid transition of Food from PENDING to COOKED", exception.getMessage());
  }

  @Test
  void nextStatusValidEndState() throws BusinessRuleValidationException {
    UUID foodId = UUID.randomUUID();
    Food food = new Food(foodId, "Soup", FoodType.BREAKFAST, 300.0f, UUID.randomUUID());
    food.nextStatus(FoodStatus.COOKING);

    food.nextStatus(FoodStatus.COOKED);

    assertEquals(FoodStatus.COOKED, food.getStatus());
  }

  @Test
  void nextStatusInvalidTransitionFromCooked() throws BusinessRuleValidationException {
    UUID foodId = UUID.randomUUID();
    Food food = new Food(foodId, "Burger", FoodType.BREAKFAST, 700.0f, UUID.randomUUID());
    food.nextStatus(FoodStatus.COOKING);
    food.nextStatus(FoodStatus.COOKED);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      food.nextStatus(FoodStatus.COOKING);
    });

    assertEquals("Invalid transition of Food from COOKED to COOKING", exception.getMessage());
  }

  @Test
  void nextStatus() throws BusinessRuleValidationException {
    // Arrange
    Food food = new Food("Cuernito", FoodType.BREAKFAST, 300.0f, UUID.randomUUID());

    // Act - Valid transition
    food.nextStatus(FoodStatus.COOKING);

    // Assert
    assertEquals(FoodStatus.COOKING, food.getStatus());

    // Act & Assert - Invalid transition
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      food.nextStatus(FoodStatus.PENDING);
    });
    assertEquals("Invalid transition of Food from COOKING to PENDING", exception.getMessage());
  }

  @Test
  void getName() throws BusinessRuleValidationException {
    // Arrange
    String expectedName = "Burger King";
    Food food = new Food(expectedName, FoodType.DINNER, 900f, UUID.randomUUID());

    // Act
    String result = food.getName();

    // Assert
    assertEquals(expectedName, result);
  }

  @Test
  void getKcal() throws BusinessRuleValidationException {
    // Arrange
    float expectedKcal = 250.0f;
    Food food = new Food("Ensalada Cesar", FoodType.BREAKFAST, expectedKcal, UUID.randomUUID());

    // Act
    float result = food.getKcal();

    // Assert
    assertEquals(expectedKcal, result);
  }

  @Test
  void getStatus() throws BusinessRuleValidationException {
    // Arrange
    Food food = new Food("Fideo", FoodType.LUNCH, 350.0f, UUID.randomUUID());

    // Act
    FoodStatus result = food.getStatus();

    // Assert
    assertEquals(FoodStatus.PENDING, result);
  }

  @Test
  void getType() throws BusinessRuleValidationException {
    // Arrange
    FoodType expectedType = FoodType.BREAKFAST;
    Food food = new Food("Panqueque", expectedType, 500.0f, UUID.randomUUID());

    // Act
    FoodType result = food.getType();

    // Assert
    assertEquals(expectedType, result);
  }

  @Test
  void getFoodPackageId() throws BusinessRuleValidationException {
    // Arrange
    UUID expectedPackageId = UUID.randomUUID();
    Food food = new Food("Sushi", FoodType.DINNER, 200.0f, expectedPackageId);

    // Act
    UUID result = food.getFoodPackageId();

    // Assert
    assertEquals(expectedPackageId, result);
  }
}