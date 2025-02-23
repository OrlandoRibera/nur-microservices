package factories.foodpackage;

import core.BusinessRuleValidationException;
import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.model.FoodType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateFoodPackageTest {

  private CreateFoodPackage createFoodPackage;
  private UUID recipeId;
  private UUID clientId;
  private UUID addressId;
  private List<Food> foods;

  @BeforeEach
  void setUp() throws BusinessRuleValidationException {
    createFoodPackage = new CreateFoodPackage();
    recipeId = UUID.randomUUID();
    clientId = UUID.randomUUID();
    addressId = UUID.randomUUID();
    foods = List.of(new Food("Apple", FoodType.BREAKFAST, 95.0f, UUID.randomUUID()));
  }

  @Test
  void testCreate_WithNewFoodPackage() {
    FoodPackage foodPackage = createFoodPackage.create(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);

    assertNotNull(foodPackage);
    assertEquals(recipeId, foodPackage.getRecipeId());
    assertEquals(clientId, foodPackage.getClientId());
    assertEquals(addressId, foodPackage.getAddressId());
    assertEquals(foods, foodPackage.getFoods());
    assertEquals(FoodPackageStatus.NEW, foodPackage.getStatus());
  }

  @Test
  void testCreate_WithExistingFoodPackageId() {
    UUID foodPackageId = UUID.randomUUID();
    FoodPackage foodPackage = createFoodPackage.create(foodPackageId, recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);

    assertNotNull(foodPackage);
    assertEquals(foodPackageId, foodPackage.getId());
    assertEquals(recipeId, foodPackage.getRecipeId());
    assertEquals(clientId, foodPackage.getClientId());
    assertEquals(addressId, foodPackage.getAddressId());
    assertEquals(foods, foodPackage.getFoods());
    assertEquals(FoodPackageStatus.NEW, foodPackage.getStatus());
  }

  @Test
  void testCreate_WithEmptyFoodList() {
    List<Food> emptyFoods = List.of();
    FoodPackage foodPackage = createFoodPackage.create(recipeId, clientId, addressId, emptyFoods, FoodPackageStatus.NEW);

    assertNotNull(foodPackage);
    assertTrue(foodPackage.getFoods().isEmpty());
  }

  @Test
  void testCreate_WithNullFoodList() {
    FoodPackage foodPackage = createFoodPackage.create(recipeId, clientId, addressId, null, FoodPackageStatus.NEW);

    assertNotNull(foodPackage);
    assertNull(foodPackage.getFoods());
  }
}