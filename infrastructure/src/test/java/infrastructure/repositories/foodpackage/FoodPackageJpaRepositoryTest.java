package infrastructure.repositories.foodpackage;

import infrastructure.model.CustomException;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageJpaModel;
import infrastructure.model.FoodPackageStatus;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FoodPackageJpaRepositoryTest {

	@Mock
	private FoodPackageCrudRepository foodPackageCrudRepository;

	@InjectMocks
	private FoodPackageJpaRepository foodPackageJpaRepository;

	private UUID foodPackageId;
	private UUID recipeId;
	private UUID clientId;
	private String address;
	private FoodPackageJpaModel foodPackageJpaModel;
	private FoodPackage foodPackage;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		foodPackageId = UUID.randomUUID();
		recipeId = UUID.randomUUID();
		clientId = UUID.randomUUID();
		address = "";

		foodPackageJpaModel = FoodPackageJpaModel.builder().id(foodPackageId).status("NEW").recipeId(recipeId).clientId(clientId).foods(new ArrayList<>()).build();

		foodPackage = new FoodPackage(foodPackageId, recipeId, clientId, address, List.of(), FoodPackageStatus.NEW);
	}

	@Test
	void testCreate() {
		FoodPackage mockFoodPackage = mock(FoodPackage.class);
		when(mockFoodPackage.getId()).thenReturn(UUID.randomUUID());
		when(mockFoodPackage.getStatus()).thenReturn(FoodPackageStatus.NEW);
		when(mockFoodPackage.getRecipeId()).thenReturn(UUID.randomUUID());
		when(mockFoodPackage.getClientId()).thenReturn(UUID.randomUUID());
		when(mockFoodPackage.getAddress()).thenReturn("");
		when(mockFoodPackage.getFoods()).thenReturn(new ArrayList<>());
		FoodPackageJpaModel jpaModel = FoodPackageJpaModel.builder().id(mockFoodPackage.getId()).status(mockFoodPackage.getStatus().name()).recipeId(mockFoodPackage.getRecipeId()).clientId(mockFoodPackage.getClientId()).address(mockFoodPackage.getAddress()).foods(new ArrayList<>()).build();
		when(foodPackageCrudRepository.save(any())).thenReturn(jpaModel);
		UUID result = foodPackageJpaRepository.create(mockFoodPackage);
		assertNotNull(result);
	}

	@Test
	void testUpdateNotFound() {
		FoodPackage mockFoodPackage = mock(FoodPackage.class);
		when(mockFoodPackage.getId()).thenReturn(UUID.randomUUID());
		when(foodPackageCrudRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(CustomException.class, () -> foodPackageJpaRepository.update(mockFoodPackage));
	}

	@Test
	void testGetAll() {
		FoodPackageJpaModel jpaModel = FoodPackageJpaModel.builder().id(UUID.randomUUID()).status("NEW").recipeId(UUID.randomUUID()).clientId(UUID.randomUUID()).address("").foods(new ArrayList<>()).build();
		when(foodPackageCrudRepository.findAll()).thenReturn(List.of(jpaModel));
		List<FoodPackage> result = foodPackageJpaRepository.getAll();
		assertEquals(0, result.size());
	}

	@Test
	void testGetNotFound() {
		when(foodPackageCrudRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(CustomException.class, () -> foodPackageJpaRepository.get(UUID.randomUUID()));
	}

	@Test
	void testFindByRecipeIdAndClientIdNotFound() {
		when(foodPackageCrudRepository.findByRecipeIdAndClientId(any(), any())).thenReturn(null);
		assertThrows(CustomException.class, () -> foodPackageJpaRepository.findByRecipeIdAndClientId(UUID.randomUUID(), UUID.randomUUID()));
	}
}
