package infrastructure.model;

import infrastructure.repositories.food.FoodCrudRepository;
import infrastructure.repositories.foodpackage.FoodPackageCrudRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;


@DataJpaTest
@ActiveProfiles("test")
class FoodJpaModelTest1 {

  @Autowired
  private FoodCrudRepository foodCrudRepository;

  @Autowired
  private FoodPackageCrudRepository foodPackageCrudRepository;

  private static FoodPackageJpaModel globalFoodPackage;
  private static FoodJpaModel globalFood;

  @BeforeAll
  static void setup() {
    globalFoodPackage = FoodPackageJpaModel.builder()
      .id(UUID.randomUUID())
      .clientId(UUID.randomUUID())
      .addressId(UUID.randomUUID())
      .recipeId(UUID.randomUUID())
      .foods(List.of())
      .status("NEW")
      .build();

    globalFood = FoodJpaModel.builder()
      .id(UUID.randomUUID())
      .name("Test food")
      .status("PENDING")
      .type("BREAKFAST")
      .kcal(100.0f)
      .foodPackage(globalFoodPackage)
      .build();
  }

  @Test
  void saveFoodForFoodPackage() {
    foodPackageCrudRepository.save(globalFoodPackage);
    foodCrudRepository.save(globalFood);

    assertEquals(globalFoodPackage.getId(), globalFood.getFoodPackage().getId());
  }

  @Test
  void testFindById() {
    foodPackageCrudRepository.save(globalFoodPackage);

    FoodJpaModel savedFood = foodCrudRepository.save(globalFood);
    Optional<FoodJpaModel> foundFood = foodCrudRepository.findById(savedFood.getId());

    assertTrue(foundFood.isPresent());
    assertEquals("Test food", foundFood.get().getName());
  }

  @Test
  void testFindAll() {
    foodPackageCrudRepository.save(globalFoodPackage);
    foodCrudRepository.save(globalFood);

    FoodJpaModel anotherFood = new FoodJpaModel();
    anotherFood.setId(UUID.randomUUID());
    anotherFood.setName("Banana");
    anotherFood.setKcal(89.0f);
    anotherFood.setType("Fruit");
    anotherFood.setStatus("Available");

    foodCrudRepository.save(anotherFood);

    List<FoodJpaModel> foods = StreamSupport.stream(foodCrudRepository.findAll().spliterator(), false).toList();
    assertEquals(2, foods.size());
  }

  @Test
  void testUpdateFood() {
    foodPackageCrudRepository.save(globalFoodPackage);
    FoodJpaModel savedFood = foodCrudRepository.save(globalFood);

    savedFood.setName("Chipilo");
    savedFood.setKcal(50.0f);
    FoodJpaModel updatedFood = foodCrudRepository.save(savedFood);

    assertEquals("Chipilo", updatedFood.getName());
    assertEquals(50.0f, updatedFood.getKcal());
  }

  @Test
  void testDeleteFood() {
    foodPackageCrudRepository.save(globalFoodPackage);
    FoodJpaModel savedFood = foodCrudRepository.save(globalFood);

    foodCrudRepository.delete(savedFood);
    Optional<FoodJpaModel> deletedFood = foodCrudRepository.findById(savedFood.getId());

    assertTrue(deletedFood.isEmpty());
  }
}