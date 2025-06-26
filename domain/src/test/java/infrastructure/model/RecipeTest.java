package infrastructure.model;

import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {
    @Test
    void constructorThrowsExceptionForNullOrEmptyIdOrClientId() {
        assertThrows(BusinessRuleValidationException.class, () -> {
            new Recipe(null, "client1", "plan");
        });
        assertThrows(BusinessRuleValidationException.class, () -> {
            new Recipe("", "client1", "plan");
        });
        assertThrows(BusinessRuleValidationException.class, () -> {
            new Recipe("id1", null, "plan");
        });
        assertThrows(BusinessRuleValidationException.class, () -> {
            new Recipe("id1", "", "plan");
        });
    }

    @Test
    void validRecipe() throws BusinessRuleValidationException {
        Recipe recipe = new Recipe("id1", "client1", "plan details");
        assertEquals("id1", recipe.getId());
        assertEquals("client1", recipe.getClientId());
        assertEquals("plan details", recipe.getPlanDetails());
    }

    @Test
    void settersAndGetters() throws BusinessRuleValidationException {
        Recipe recipe = new Recipe();
        recipe.setId("id2");
        recipe.setClientId("client2");
        recipe.setPlanDetails("plan2");
        assertEquals("id2", recipe.getId());
        assertEquals("client2", recipe.getClientId());
        assertEquals("plan2", recipe.getPlanDetails());
    }
}