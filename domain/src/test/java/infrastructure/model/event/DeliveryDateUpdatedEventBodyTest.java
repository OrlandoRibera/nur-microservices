package infrastructure.model.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryDateUpdatedEventBodyTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        DeliveryDateUpdatedEventBody body = new DeliveryDateUpdatedEventBody();
        body.setClientGuid("Client");
        body.setFromDate("Prev");
        body.setToDate("New");
        body.setAddressGuid("Addr");
        assertEquals("Client", body.getClientGuid());
        assertEquals("Prev", body.getFromDate());
        assertEquals("New", body.getToDate());
        assertEquals("Addr", body.getAddressGuid());
    }

    @Test
    void testAllArgsConstructor() {
        DeliveryDateUpdatedEventBody body = new DeliveryDateUpdatedEventBody("Client", "Prev", "New", "Addr");
        assertEquals("Client", body.getClientGuid());
        assertEquals("Prev", body.getFromDate());
        assertEquals("New", body.getToDate());
        assertEquals("Addr", body.getAddressGuid());
    }
}
