package dto;

import infrastructure.model.FoodPackageStatus;

import java.util.List;

// TODO: Check if id is necessary
public record FoodPackageDTO(String id, String recipeId, String clientId, String addressId, List<FoodDTO> foods,
                             FoodPackageStatus status) {
}
