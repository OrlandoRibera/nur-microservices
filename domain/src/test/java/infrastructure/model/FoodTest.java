package infrastructure.model;

import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class FoodTest {

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
}