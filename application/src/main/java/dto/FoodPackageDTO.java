package dto;

import infrastructure.model.FoodPackageStatus;

import java.util.List;

public record FoodPackageDTO(String id, String recipeId, String clientId, String address, List<FoodDTO> foods,
							 FoodPackageStatus status) {
}
