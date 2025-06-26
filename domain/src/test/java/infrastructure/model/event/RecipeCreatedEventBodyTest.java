package infrastructure.model.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeCreatedEventBodyTest {
    @Test
    void testAllArgsConstructorAndSetters() {
        RecipeCreatedEventBody body = new RecipeCreatedEventBody("id", "created", "contract", "client", "order",
                "plan");
        assertEquals("id", body.getId());
        assertEquals("created", body.getCreatedAt());
        assertEquals("contract", body.getContractId());
        assertEquals("client", body.getClientId());
        assertEquals("order", body.getOrderId());
        assertEquals("plan", body.getPlanDetails());

        body.setId("id2");
        body.setCreatedAt("created2");
        body.setContractId("contract2");
        body.setClientId("client2");
        body.setOrderId("order2");
        body.setPlanDetails("plan2");
        assertEquals("id2", body.getId());
        assertEquals("created2", body.getCreatedAt());
        assertEquals("contract2", body.getContractId());
        assertEquals("client2", body.getClientId());
        assertEquals("order2", body.getOrderId());
        assertEquals("plan2", body.getPlanDetails());
    }
}