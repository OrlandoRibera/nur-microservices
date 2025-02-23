package factories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodStatus;
import infrastructure.model.FoodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateFoodTest {
  CreateFood createFood;
  UUID foodPackageId;
  UUID foodId;

  @BeforeEach
  void setUp() {
    createFood = new CreateFood();
    foodPackageId = UUID.randomUUID();
    foodId = UUID.randomUUID();
  }

  @Test
  void testCreate() throws BusinessRuleValidationException {
    Food food = createFood.create("Pizza", FoodType.BREAKFAST, FoodStatus.PENDING, 95.0f, foodPackageId);

    assertNotNull(food);
    assertEquals("Pizza", food.getName());
    assertEquals(FoodType.BREAKFAST, food.getType());
    assertEquals(FoodStatus.PENDING, food.getStatus());
    assertEquals(95.0f, food.getKcal());
    assertEquals(foodPackageId, food.getFoodPackageId());
  }

  @Test
  void testCreate_WithId() throws BusinessRuleValidationException {
    Food food = createFood.create(foodId, "Panchito", FoodType.LUNCH, FoodStatus.COOKED, 105.0f, foodPackageId);

    assertNotNull(food);
    assertEquals(foodId, food.getId());
    assertEquals("Panchito", food.getName());
    assertEquals(FoodType.LUNCH, food.getType());
    assertEquals(FoodStatus.COOKED, food.getStatus());
    assertEquals(105.0f, food.getKcal());
    assertEquals(foodPackageId, food.getFoodPackageId());
  }

  @Test
  void testCreate_InvalidName() {
    assertThrows(BusinessRuleValidationException.class, () -> {
      createFood.create(null, FoodType.BREAKFAST, FoodStatus.PENDING, 95.0f, foodPackageId);
    });

    assertThrows(BusinessRuleValidationException.class, () -> {
      createFood.create("", FoodType.BREAKFAST, FoodStatus.PENDING, 95.0f, foodPackageId);
    });
  }

  @Test
  void testCreate_WithId_InvalidName() {
    assertThrows(BusinessRuleValidationException.class, () -> {
      createFood.create(foodId, null, FoodType.BREAKFAST, FoodStatus.PENDING, 95.0f, foodPackageId);
    });

    assertThrows(BusinessRuleValidationException.class, () -> {
      createFood.create(foodId, "", FoodType.BREAKFAST, FoodStatus.PENDING, 95.0f, foodPackageId);
    });
  }
}