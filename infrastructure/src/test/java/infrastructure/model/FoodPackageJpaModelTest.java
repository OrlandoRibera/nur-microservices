package infrastructure.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FoodPackageJpaModelTest {

  private FoodPackageJpaModel foodPackage;
  private UUID id;
  private UUID clientId;
  private UUID addressId;
  private UUID recipeId;

  @BeforeEach
  void setUp() {
    id = UUID.randomUUID();
    clientId = UUID.randomUUID();
    addressId = UUID.randomUUID();
    recipeId = UUID.randomUUID();
    foodPackage = FoodPackageJpaModel.builder()
      .id(id)
      .clientId(clientId)
      .addressId(addressId)
      .recipeId(recipeId)
      .status("NEW")
      .foods(new ArrayList<>())
      .build();
  }

  @Test
  void testConstructorAndGetters() {
    assertEquals(id, foodPackage.getId());
    assertEquals(clientId, foodPackage.getClientId());
    assertEquals(addressId, foodPackage.getAddressId());
    assertEquals(recipeId, foodPackage.getRecipeId());
    assertEquals("NEW", foodPackage.getStatus());
    assertTrue(foodPackage.getFoods().isEmpty());
  }

  @Test
  void testSetters() {
    UUID newId = UUID.randomUUID();
    foodPackage.setId(newId);
    foodPackage.setStatus("DELIVERED");

    assertEquals(newId, foodPackage.getId());
    assertEquals("DELIVERED", foodPackage.getStatus());
  }

  @Test
  void testAddFood() {
    FoodJpaModel food = FoodJpaModel.builder()
      .id(UUID.randomUUID())
      .name("Test Food")
      .status("PENDING")
      .type("BREAKFAST")
      .kcal(100.0f)
      .build();

    foodPackage.getFoods().add(food);

    assertEquals(1, foodPackage.getFoods().size());
    assertEquals("Test Food", foodPackage.getFoods().get(0).getName());
  }

  @Test
  void testRemoveFood() {
    FoodJpaModel food = FoodJpaModel.builder()
      .id(UUID.randomUUID())
      .name("Test Food")
      .status("PENDING")
      .type("BREAKFAST")
      .kcal(100.0f)
      .build();

    foodPackage.getFoods().add(food);
    assertEquals(1, foodPackage.getFoods().size());

    foodPackage.getFoods().remove(food);
    assertTrue(foodPackage.getFoods().isEmpty());
  }

  @Test
  void testNoArgsConstructor() {
    FoodPackageJpaModel emptyPackage = new FoodPackageJpaModel();
    assertNull(emptyPackage.getId());
    assertNull(emptyPackage.getStatus());
    assertNotNull(emptyPackage.getFoods());
  }
}
