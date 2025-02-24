package event;

import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodPackageEventsTest {

  @Test
  void getFoodPackageDispatched() {
    // Arrange
    UUID recipeId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID addressId = UUID.randomUUID();
    FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, new ArrayList<>(), FoodPackageStatus.NEW);
    FoodPackageDispatched foodPackageDispatched = new FoodPackageDispatched(foodPackage);

    // Act
    FoodPackage result = foodPackageDispatched.getFoodPackage();

    // Assert
    assertEquals(foodPackage, result);
  }

  @Test
  void getFoodPackagePacked() {
    // Arrange
    UUID recipeId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID addressId = UUID.randomUUID();
    FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, new ArrayList<>(), FoodPackageStatus.NEW);
    FoodPackagePacked foodPackagePacked = new FoodPackagePacked(foodPackage);

    // Act
    FoodPackage result = foodPackagePacked.getFoodPackage();

    // Assert
    assertEquals(foodPackage, result);
  }
}