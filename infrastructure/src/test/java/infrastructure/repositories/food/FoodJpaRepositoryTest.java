package infrastructure.repositories.food;

import core.BusinessRuleValidationException;
import infrastructure.model.*;
import infrastructure.repositories.foodpackage.FoodPackageCrudRepository;
import infrastructure.utils.FoodUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FoodJpaRepositoryTest {

  @Mock
  private FoodCrudRepository foodCrudRepository;

  @Mock
  private FoodPackageCrudRepository foodPackageCrudRepository;

  @InjectMocks
  private FoodJpaRepository foodJpaRepository;

  private UUID foodId;
  private UUID foodPackageId;
  private FoodJpaModel foodJpaModel;
  private Food food;
  private FoodPackageJpaModel foodPackageJpaModel;

  @BeforeEach
  void setUp() throws BusinessRuleValidationException {
    MockitoAnnotations.openMocks(this);

    foodId = UUID.randomUUID();
    foodPackageId = UUID.randomUUID();

    foodPackageJpaModel = FoodPackageJpaModel.builder()
      .id(foodPackageId)
      .build();

    foodJpaModel = new FoodJpaModel();
    foodJpaModel.setId(foodId);
    foodJpaModel.setName("Pizza");
    foodJpaModel.setStatus("PENDING");
    foodJpaModel.setKcal(300.0f);
    foodJpaModel.setType("BREAKFAST");
    foodJpaModel.setFoodPackage(foodPackageJpaModel);

    food = new Food(foodId, "Pizza", FoodType.BREAKFAST, 300.0f, foodPackageId);
  }

  @Test
  void testGetFoodSuccess() throws BusinessRuleValidationException {
    when(foodCrudRepository.findById(foodId)).thenReturn(Optional.of(foodJpaModel));

    Food result = foodJpaRepository.get(foodId);

    assertNotNull(result);
    assertEquals(foodId, result.getId());
    assertEquals("Pizza", result.getName());
  }

  @Test
  void testGetFoodThrowsCustomException() {
    when(foodCrudRepository.findById(foodId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      foodJpaRepository.get(foodId);
    });

    assertEquals("Food not found", exception.getMessage());
  }

  @Test
  void testCreateFood() {
    when(foodCrudRepository.save(any(FoodJpaModel.class))).thenReturn(foodJpaModel);

    UUID result = foodJpaRepository.create(food);

    assertNotNull(result);
    assertEquals(foodId, result);
    verify(foodCrudRepository, times(1)).save(any(FoodJpaModel.class));
  }

  @Test
  void testUpdateFoodSuccess() {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));
    when(foodCrudRepository.save(any())).thenReturn(foodJpaModel);

    UUID result = foodJpaRepository.update(food);

    assertNotNull(result);
    assertEquals(foodId, result);
  }

  @Test
  void testUpdateFoodThrowsCustomException() {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      foodJpaRepository.update(food);
    });

    assertEquals("Food package not found", exception.getMessage());
  }

  @Test
  void testFindByFoodPackageIdSuccess() throws BusinessRuleValidationException {
    List<FoodJpaModel> jpaModels = new ArrayList<>();
    jpaModels.add(foodJpaModel);

    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));
    when(foodCrudRepository.findByFoodPackageId(foodPackageId)).thenReturn(jpaModels);

    List<Food> result = foodJpaRepository.findByFoodPackageId(foodPackageId);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(foodId, result.get(0).getId());
  }

  @Test
  void testFindByFoodPackageIdEmptyList() throws BusinessRuleValidationException {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));
    when(foodCrudRepository.findByFoodPackageId(foodPackageId)).thenReturn(Collections.emptyList());

    List<Food> result = foodJpaRepository.findByFoodPackageId(foodPackageId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindByFoodPackageIdNull() throws BusinessRuleValidationException {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));
    when(foodCrudRepository.findByFoodPackageId(foodPackageId)).thenReturn(null);

    List<Food> result = foodJpaRepository.findByFoodPackageId(foodPackageId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindByFoodPackageIdPackageNotFound() throws BusinessRuleValidationException {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.empty());

    List<Food> result = foodJpaRepository.findByFoodPackageId(foodPackageId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
