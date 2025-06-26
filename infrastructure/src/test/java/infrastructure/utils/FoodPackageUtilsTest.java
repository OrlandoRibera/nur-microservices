package infrastructure.utils;

import infrastructure.model.*;
import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FoodPackageUtilsTest {
    @Test
    void testJpaModelToFoodPackage() throws BusinessRuleValidationException {
        FoodPackageJpaModel parentPackage = FoodPackageJpaModel.builder()
                .id(UUID.randomUUID())
                .recipeId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .addressId(UUID.randomUUID())
                .status(FoodPackageStatus.NEW.toString())
                .build();
        FoodJpaModel foodJpaModel = FoodJpaModel.builder()
                .id(UUID.randomUUID())
                .name("Pizza")
                .kcal(300.0f)
                .type(FoodType.LUNCH.toString())
                .status(FoodStatus.PENDING.toString())
                .foodPackage(parentPackage)
                .build();
        List<FoodJpaModel> foods = new ArrayList<>();
        foods.add(foodJpaModel);
        parentPackage.setFoods(foods);
        FoodPackageJpaModel jpaModel = parentPackage;
        FoodPackage foodPackage = FoodPackageUtils.jpaModelToFoodPackage(jpaModel);
        assertEquals(jpaModel.getId(), foodPackage.getId());
        assertEquals(jpaModel.getRecipeId(), foodPackage.getRecipeId());
        assertEquals(jpaModel.getClientId(), foodPackage.getClientId());
        assertEquals(jpaModel.getAddressId(), foodPackage.getAddressId());
        assertEquals(FoodPackageStatus.valueOf(jpaModel.getStatus()), foodPackage.getStatus());
        assertEquals(1, foodPackage.getFoods().size());
    }

    @Test
    void testFoodPackageToJpaEntity() throws BusinessRuleValidationException {
        FoodPackage foodPackage = new FoodPackage(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new ArrayList<>(), FoodPackageStatus.NEW);
        List<FoodJpaModel> foods = new ArrayList<>();
        FoodPackageJpaModel jpaModel = FoodPackageUtils.foodPackageToJpaEntity(foodPackage, foods);
        assertEquals(foodPackage.getId(), jpaModel.getId());
        assertEquals(foodPackage.getRecipeId(), jpaModel.getRecipeId());
        assertEquals(foodPackage.getClientId(), jpaModel.getClientId());
        assertEquals(foodPackage.getAddressId(), jpaModel.getAddressId());
        assertEquals(foods, jpaModel.getFoods());
    }
}