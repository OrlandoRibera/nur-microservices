package mappers;

import core.BusinessRuleValidationException;
import dto.FoodDTO;
import infrastructure.model.Food;
import infrastructure.model.FoodStatus;
import infrastructure.model.FoodType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class FoodMapperTest {

  @Test
  void fromFoodToDTO() throws BusinessRuleValidationException {
    Food food = new Food(UUID.randomUUID(), "Pizza", FoodType.LUNCH, FoodStatus.PENDING, 100.0f, UUID.randomUUID());
    FoodDTO foodDTO = FoodMapper.from(food);

    assertNotNull(FoodMapper.class);
    assertEquals(UUID.fromString(foodDTO.foodId()), food.getId());
  }

  @Test
  void testFromDTOToFood() throws BusinessRuleValidationException {
    FoodDTO foodDTO = new FoodDTO(UUID.randomUUID().toString(), "Pizza", "PENDING", "LUNCH", 191.0f, UUID.randomUUID().toString());
    Food food = FoodMapper.from(foodDTO);

    assertNotNull(FoodMapper.class);
    assertEquals(food.getName(), foodDTO.name());
  }
}