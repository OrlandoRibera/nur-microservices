package infrastructure.repositories.foodpackage;

import core.BusinessRuleValidationException;
import infrastructure.model.*;
import infrastructure.utils.FoodPackageUtils;
import infrastructure.utils.FoodUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FoodPackageJpaRepositoryTest {

  @Mock
  private FoodPackageCrudRepository foodPackageCrudRepository;

  @InjectMocks
  private FoodPackageJpaRepository foodPackageJpaRepository;

  private UUID foodPackageId;
  private UUID recipeId;
  private UUID clientId;
  private UUID addressId;
  private FoodPackageJpaModel foodPackageJpaModel;
  private FoodPackage foodPackage;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    foodPackageId = UUID.randomUUID();
    recipeId = UUID.randomUUID();
    clientId = UUID.randomUUID();
    addressId = UUID.randomUUID();

    foodPackageJpaModel = FoodPackageJpaModel.builder()
      .id(foodPackageId)
      .status("NEW")
      .recipeId(recipeId)
      .clientId(clientId)
      .foods(new ArrayList<>())
      .build();

    foodPackage = new FoodPackage(foodPackageId, recipeId, clientId, addressId, List.of(), FoodPackageStatus.NEW);
  }

  @Test
  void testCreateFoodPackage() {
    when(foodPackageCrudRepository.save(any(FoodPackageJpaModel.class))).thenReturn(foodPackageJpaModel);

    UUID result = foodPackageJpaRepository.create(foodPackage);

    assertNotNull(result);
    assertEquals(foodPackageId, result);
    verify(foodPackageCrudRepository, times(1)).save(any(FoodPackageJpaModel.class));
  }

  @Test
  void testUpdateFoodPackageSuccess() {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));
    when(foodPackageCrudRepository.save(foodPackageJpaModel)).thenReturn(foodPackageJpaModel);

    UUID result = foodPackageJpaRepository.update(foodPackage);

    assertNotNull(result);
    assertEquals(foodPackageId, result);
  }

  @Test
  void testUpdateFoodPackageThrowsCustomException() {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      foodPackageJpaRepository.update(foodPackage);
    });

    assertEquals("Food package not found", exception.getMessage());
  }

  @Test
  void testGetAllFoodPackages() {
    List<FoodPackageJpaModel> jpaModels = new ArrayList<>();
    jpaModels.add(foodPackageJpaModel);

    when(foodPackageCrudRepository.findAll()).thenReturn(jpaModels);

    List<FoodPackage> result = foodPackageJpaRepository.getAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(foodPackageId, result.get(0).getId());
  }

  @Test
  void testGetAllFoodPackagesEmptyList() {
    when(foodPackageCrudRepository.findAll()).thenReturn(new ArrayList<>());

    List<FoodPackage> result = foodPackageJpaRepository.getAll();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testGetFoodPackageSuccess() throws BusinessRuleValidationException {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.of(foodPackageJpaModel));

    FoodPackage result = foodPackageJpaRepository.get(foodPackageId);

    assertNotNull(result);
    assertEquals(foodPackageId, result.getId());
  }

  @Test
  void testGetFoodPackageThrowsCustomException() {
    when(foodPackageCrudRepository.findById(foodPackageId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      foodPackageJpaRepository.get(foodPackageId);
    });

    assertEquals("Food package not found", exception.getMessage());
  }

  @Test
  void testFindByRecipeIdAndClientIdSuccess() throws BusinessRuleValidationException {
    when(foodPackageCrudRepository.findByRecipeIdAndClientId(recipeId, clientId)).thenReturn(foodPackageJpaModel);

    FoodPackage result = foodPackageJpaRepository.findByRecipeIdAndClientId(recipeId, clientId);

    assertNotNull(result);
    assertEquals(foodPackageId, result.getId());
  }

  @Test
  void testFindByRecipeIdAndClientIdThrowsCustomException() {
    when(foodPackageCrudRepository.findByRecipeIdAndClientId(recipeId, clientId)).thenReturn(null);

    CustomException exception = assertThrows(CustomException.class, () -> {
      foodPackageJpaRepository.findByRecipeIdAndClientId(recipeId, clientId);
    });

    assertEquals("Food package not found", exception.getMessage());
  }
}
