package infrastructure.utils;

import infrastructure.model.Recipe;
import infrastructure.model.RecipeJpaModel;
import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeUtilsTest {
    @Test
    void testJpaMotelToRecipe() throws BusinessRuleValidationException {
        RecipeJpaModel jpaModel = RecipeJpaModel.builder()
                .id("id1")
                .clientId("client1")
                .planDetails("plan")
                .build();
        Recipe recipe = RecipeUtils.jpaMotelToRecipe(jpaModel);
        assertNotNull(recipe);
        assertEquals(jpaModel.getId(), recipe.getId());
        assertEquals(jpaModel.getClientId(), recipe.getClientId());
        assertEquals(jpaModel.getPlanDetails(), recipe.getPlanDetails());
    }
}