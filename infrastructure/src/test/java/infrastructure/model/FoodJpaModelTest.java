package infrastructure.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class FoodJpaModelTest {

  private FoodJpaModel food;
  private UUID id;
  private UUID foodPackageId;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    foodPackageId = UUID.randomUUID();

    FoodPackageJpaModel foodPackage = FoodPackageJpaModel.builder()
        .id(foodPackageId)
        .build();

    food = FoodJpaModel.builder()
        .id(id)
        .name("Test Food")
        .status("PENDING")
        .type("BREAKFAST")
        .kcal(100.0f)
        .foodPackage(foodPackage)
        .build();
  }

  @Test
  void testConstructorAndGetters() {
    assertEquals(id, food.getId());
    assertEquals("Test Food", food.getName());
    assertEquals("PENDING", food.getStatus());
    assertEquals("BREAKFAST", food.getType());
    assertEquals(100.0f, food.getKcal());
    assertEquals(foodPackageId, food.getFoodPackage().getId());
  }

  @Test
  void testSetters() {
    food.setName("Updated Food");
    food.setStatus("AVAILABLE");
    food.setType("LUNCH");
    food.setKcal(250.0f);

    assertEquals("Updated Food", food.getName());
    assertEquals("AVAILABLE", food.getStatus());
    assertEquals("LUNCH", food.getType());
    assertEquals(250.0f, food.getKcal());
  }

  @Test
  void testNoArgsConstructor() {
    FoodJpaModel emptyFood = new FoodJpaModel();
    assertNull(emptyFood.getId());
    assertNull(emptyFood.getName());
    assertNull(emptyFood.getStatus());
    assertNull(emptyFood.getType());
    assertEquals(0.0f, emptyFood.getKcal());
    assertNull(emptyFood.getFoodPackage());
  }

  @Test
  void testBuilderAndGettersSetters() {
    UUID id = UUID.randomUUID();
    FoodJpaModel model = FoodJpaModel.builder()
        .id(id)
        .name("Pizza")
        .kcal(300.0f)
        .type("LUNCH")
        .status("PENDING")
        .build();
    assertEquals(id, model.getId());
    assertEquals("Pizza", model.getName());
    assertEquals(300.0f, model.getKcal());
    assertEquals("LUNCH", model.getType());
    assertEquals("PENDING", model.getStatus());
    model.setName("Burger");
    assertEquals("Burger", model.getName());
  }
}
