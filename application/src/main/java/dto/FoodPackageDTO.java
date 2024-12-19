package dto;

import infrastructure.model.FoodPackageStatus;

import java.util.List;

public record FoodPackageDTO(String id, String recipeId, String clientId, String addressId, List<FoodDTO> foods,
                             FoodPackageStatus status) {
}
