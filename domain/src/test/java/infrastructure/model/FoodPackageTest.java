package infrastructure.model;

import core.BusinessRuleValidationException;
import event.FoodPackageDispatched;
import event.FoodPackagePacked;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FoodPackageTest {

  @Test
  void nextStatusValidTransition() {

    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.COOKED
    );

    foodPackage.nextStatus(FoodPackageStatus.PACKED);

    assertEquals(FoodPackageStatus.PACKED, foodPackage.getStatus());
  }

  @Test
  void nextStatusInvalidTransitionThrowsException() {
    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.NEW
    );

    assertThrows(IllegalStateException.class, () -> foodPackage.nextStatus(FoodPackageStatus.DISPATCHED));
  }

  @Test
  void nextStatusTriggersPackedEvent() {
    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.COOKED
    );

    foodPackage.nextStatus(FoodPackageStatus.PACKED);

    assertTrue(foodPackage.getDomainEvents().stream().anyMatch(event -> event instanceof FoodPackagePacked));
  }

  @Test
  void nextStatusTriggersDispatchedEvent() {
    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.PACKED
    );

    foodPackage.nextStatus(FoodPackageStatus.DISPATCHED);

    assertTrue(foodPackage.getDomainEvents().stream().anyMatch(event -> event instanceof FoodPackageDispatched));
  }

  @Test
  void createFoodAddsNewFoodToList() throws BusinessRuleValidationException {
    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.NEW
    );

    foodPackage.createFood("Pizza", FoodType.BREAKFAST, 300);

    assertEquals(1, foodPackage.getFoods().size());
    assertEquals("Pizza", foodPackage.getFoods().get(0).getName());
  }

  @Test
  void createFoodThrowsExceptionForInvalidFood() {
    FoodPackage foodPackage = new FoodPackage(
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      new ArrayList<>(),
      FoodPackageStatus.NEW
    );

    assertThrows(BusinessRuleValidationException.class, () -> foodPackage.createFood("Pizza", FoodType.BREAKFAST, -10));
  }
}
