package infrastructure.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeJpaModelTest {
    @Test
    void testBuilderGettersSetters() {
        RecipeJpaModel model = RecipeJpaModel.builder()
                .id("id1")
                .clientId("client1")
                .planDetails("plan")
                .build();
        assertEquals("id1", model.getId());
        assertEquals("client1", model.getClientId());
        assertEquals("plan", model.getPlanDetails());
        model.setPlanDetails("plan2");
        assertEquals("plan2", model.getPlanDetails());
    }
}