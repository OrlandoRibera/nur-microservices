package infrastructure.model;

import infrastructure.repositories.food.FoodCrudRepository;
import infrastructure.repositories.foodpackage.FoodPackageCrudRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@DataJpaTest
@ActiveProfiles("test")
class FoodPackageJpaModelTest1 {

  @Autowired
  private FoodCrudRepository foodCrudRepository;

  @Autowired
  private FoodPackageCrudRepository foodPackageCrudRepository;

  private static FoodPackageJpaModel globalFoodPackage;

  @BeforeAll
  static void setup() {
    globalFoodPackage = FoodPackageJpaModel.builder().id(UUID.randomUUID()).clientId(UUID.randomUUID()).addressId(UUID.randomUUID()).recipeId(UUID.randomUUID()).status("NEW").foods(List.of()).build();
  }

  @Test
  void saveFoodPackage() {
    foodPackageCrudRepository.save(globalFoodPackage);

    assertNotNull(globalFoodPackage.getId());
  }

  @Test
  void testFindById() {
    FoodPackageJpaModel savedPackage = foodPackageCrudRepository.save(globalFoodPackage);
    Optional<FoodPackageJpaModel> foundPackage = foodPackageCrudRepository.findById(savedPackage.getId());

    assertTrue(foundPackage.isPresent());
    assertEquals("NEW", foundPackage.get().getStatus());
  }

  @Test
  void testFindAll() {
    foodPackageCrudRepository.save(globalFoodPackage);

    FoodPackageJpaModel anotherPackage = FoodPackageJpaModel.builder().id(UUID.randomUUID()).clientId(UUID.randomUUID()).addressId(UUID.randomUUID()).recipeId(UUID.randomUUID()).status("SHIPPED").build();

    foodPackageCrudRepository.save(anotherPackage);

    List<FoodPackageJpaModel> packages = StreamSupport.stream(foodPackageCrudRepository.findAll().spliterator(), false).toList();
    assertEquals(2, packages.size());
  }

  @Test
  void testUpdateFoodPackage() {
    FoodPackageJpaModel savedPackage = foodPackageCrudRepository.save(globalFoodPackage);

    savedPackage.setStatus("DELIVERED");
    FoodPackageJpaModel updatedPackage = foodPackageCrudRepository.save(savedPackage);

    assertEquals("DELIVERED", updatedPackage.getStatus());
  }

  @Test
  void testDeleteFoodPackage() {
    FoodPackageJpaModel savedPackage = foodPackageCrudRepository.save(globalFoodPackage);

    foodPackageCrudRepository.delete(savedPackage);
    Optional<FoodPackageJpaModel> deletedPackage = foodPackageCrudRepository.findById(savedPackage.getId());

    assertTrue(deletedPackage.isEmpty());
  }

  @Test
  void testCascadeDeleteFoods() {
    FoodJpaModel foodJpaModel = FoodJpaModel.builder().id(UUID.randomUUID()).name("Test food").status("PENDING").type("BREAKFAST").kcal(100.0f).foodPackage(globalFoodPackage).build();

    List<FoodJpaModel> newFoods = new ArrayList<>(globalFoodPackage.getFoods());
    newFoods.add(foodJpaModel);
    globalFoodPackage.setFoods(newFoods);
    foodPackageCrudRepository.save(globalFoodPackage);

    assertEquals(1, globalFoodPackage.getFoods().size());

    // Al eliminar el paquete, también se deben eliminar los alimentos (orphanRemoval = true)
    foodPackageCrudRepository.delete(globalFoodPackage);
    Optional<FoodJpaModel> deletedFood = foodCrudRepository.findById(foodJpaModel.getId());

    assertTrue(deletedFood.isEmpty());
  }
}
