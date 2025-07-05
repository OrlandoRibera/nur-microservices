package infrastructure.model.event;

import infrastructure.model.Food;
import infrastructure.model.FoodPackage;
import infrastructure.model.FoodPackageStatus;
import infrastructure.model.FoodType;
import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class LightFoodPackageTest {
    @Test
    void testConstructorFromFoodPackage() throws BusinessRuleValidationException {
        UUID recipeId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        String addressId = "";
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Pizza", FoodType.LUNCH, 300.0f, UUID.randomUUID()));
        foods.add(new Food("Salad", FoodType.DINNER, 150.0f, UUID.randomUUID()));
        FoodPackage foodPackage = new FoodPackage(recipeId, clientId, addressId, foods, FoodPackageStatus.NEW);
        LightFoodPackage light = new LightFoodPackage(foodPackage);
        assertEquals(foodPackage.getId().toString(), light.getId());
        assertEquals(foodPackage.getStatus().name(), light.getStatus());
        assertEquals(List.of("Pizza", "Salad"), light.getFoods());
    }
}
