package infrastructure.repositories.foodpackage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import infrastructure.model.CustomException;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageJpaModel;
import infrastructure.model.FoodPackageStatus;

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
  void testCreate() {
    FoodPackage foodPackage = mock(FoodPackage.class);
    when(foodPackage.getId()).thenReturn(UUID.randomUUID());
    when(foodPackage.getStatus()).thenReturn(FoodPackageStatus.NEW);
    when(foodPackage.getRecipeId()).thenReturn(UUID.randomUUID());
    when(foodPackage.getClientId()).thenReturn(UUID.randomUUID());
    when(foodPackage.getAddressId()).thenReturn(UUID.randomUUID());
    when(foodPackage.getFoods()).thenReturn(new ArrayList<>());
    FoodPackageJpaModel jpaModel = FoodPackageJpaModel.builder().id(foodPackage.getId())
        .status(foodPackage.getStatus().name()).recipeId(foodPackage.getRecipeId()).clientId(foodPackage.getClientId())
        .addressId(foodPackage.getAddressId()).foods(new ArrayList<>()).build();
    when(foodPackageCrudRepository.save(any())).thenReturn(jpaModel);
    UUID result = foodPackageJpaRepository.create(foodPackage);
    assertNotNull(result);
  }

  @Test
  void testUpdateNotFound() {
    FoodPackage foodPackage = mock(FoodPackage.class);
    when(foodPackage.getId()).thenReturn(UUID.randomUUID());
    when(foodPackageCrudRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(CustomException.class, () -> foodPackageJpaRepository.update(foodPackage));
  }

  @Test
  void testGetAll() {
    FoodPackageJpaModel jpaModel = FoodPackageJpaModel.builder().id(UUID.randomUUID()).status("NEW")
        .recipeId(UUID.randomUUID()).clientId(UUID.randomUUID()).addressId(UUID.randomUUID()).foods(new ArrayList<>())
        .build();
    when(foodPackageCrudRepository.findAll()).thenReturn(List.of(jpaModel));
    List<FoodPackage> result = foodPackageJpaRepository.getAll();
    assertEquals(1, result.size());
  }

  @Test
  void testGetNotFound() {
    when(foodPackageCrudRepository.findById(any())).thenReturn(Optional.empty());
    assertThrows(CustomException.class, () -> foodPackageJpaRepository.get(UUID.randomUUID()));
  }

  @Test
  void testFindByRecipeIdAndClientIdNotFound() {
    when(foodPackageCrudRepository.findByRecipeIdAndClientId(any(), any())).thenReturn(null);
    assertThrows(CustomException.class,
        () -> foodPackageJpaRepository.findByRecipeIdAndClientId(UUID.randomUUID(), UUID.randomUUID()));
  }
}
