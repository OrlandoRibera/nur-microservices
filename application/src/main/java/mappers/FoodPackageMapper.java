package mappers;

import dto.FoodDTO;
import dto.FoodPackageDTO;
import infrastructure.model.FoodPackage;

import java.util.ArrayList;
import java.util.List;

public final class FoodPackageMapper {
  public static FoodPackageDTO from(FoodPackage foodPackage) {
    List<FoodDTO> foodDTOS = (foodPackage.getFoods() == null) ? new ArrayList<>() : foodPackage.getFoods().stream().map(FoodMapper::from).toList();
    return new FoodPackageDTO(foodPackage.getId().toString(), foodPackage.getRecipeId().toString(), foodPackage.getClientId().toString(), foodPackage.getAddressId().toString(), foodDTOS, foodPackage.getStatus());
  }
}
