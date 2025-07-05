package command.getFoodPackages;

import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.repositories.FoodPackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetFoodPackagesHandlerTest {

	@Mock
	private FoodPackageRepository foodPackageRepository;

	@InjectMocks
	private GetFoodPackagesHandler handler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldGetSuccessfully() {
		GetFoodPackagesCommand command = new GetFoodPackagesCommand();
		List<FoodPackage> foodPackages = new ArrayList<>();
		foodPackages.add(new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "", List.of(), FoodPackageStatus.NEW));
		foodPackages.add(new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "", List.of(), FoodPackageStatus.NEW));

		when(foodPackageRepository.getAll()).thenReturn(foodPackages);

		List<FoodPackageDTO> result = handler.handle(command);
		assertEquals(result.size(), foodPackages.size());
	}

	@Test
	void shouldReturnEmptyList() {
		GetFoodPackagesCommand command = new GetFoodPackagesCommand();
		when(foodPackageRepository.getAll()).thenReturn(List.of());

		List<FoodPackageDTO> result = handler.handle(command);

		assertEquals(0, result.size());
	}
}
