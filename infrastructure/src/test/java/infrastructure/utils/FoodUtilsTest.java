package infrastructure.utils;

import infrastructure.model.*;
import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class FoodUtilsTest {
    @Test
    void testFoodToJpaEntityAndJpaToFood() throws BusinessRuleValidationException {
        Food food = new Food(UUID.randomUUID(), "Pizza", FoodType.LUNCH, FoodStatus.PENDING, 300.0f, UUID.randomUUID());
        FoodPackageJpaModel foodPackageJpaModel = new FoodPackageJpaModel(food.getFoodPackageId());
        FoodJpaModel jpaModel = FoodUtils.foodToJpaEntity(food, foodPackageJpaModel);
        assertEquals(food.getName(), jpaModel.getName());
        assertEquals(food.getType().toString(), jpaModel.getType());
        assertEquals(food.getStatus().toString(), jpaModel.getStatus());
        assertEquals(food.getKcal(), jpaModel.getKcal());
        assertEquals(food.getFoodPackageId(), jpaModel.getFoodPackage().getId());

        Food foodFromJpa = FoodUtils.jpaToFood(jpaModel);
        assertEquals(food.getName(), foodFromJpa.getName());
        assertEquals(food.getType(), foodFromJpa.getType());
        assertEquals(food.getStatus(), foodFromJpa.getStatus());
        assertEquals(food.getKcal(), foodFromJpa.getKcal());
        assertEquals(food.getFoodPackageId(), foodFromJpa.getFoodPackageId());
    }

    @Test
    void testFoodToJpaEntities() throws BusinessRuleValidationException {
        Food food1 = new Food(UUID.randomUUID(), "Pizza", FoodType.LUNCH, FoodStatus.PENDING, 300.0f,
                UUID.randomUUID());
        Food food2 = new Food(UUID.randomUUID(), "Salad", FoodType.DINNER, FoodStatus.COOKED, 150.0f,
                UUID.randomUUID());
        FoodPackageJpaModel foodPackageJpaModel = new FoodPackageJpaModel(food1.getFoodPackageId());
        List<FoodJpaModel> jpaModels = FoodUtils.foodToJpaEntities(List.of(food1, food2), foodPackageJpaModel);
        assertEquals(2, jpaModels.size());
    }
}