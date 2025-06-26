package infrastructure.model.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressUpdatedEventBodyTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        AddressUpdatedEventBody body = new AddressUpdatedEventBody();
        body.setCity("City");
        body.setStreet("Street");
        body.setAddressId("AddrId");
        body.setLongitude("Long");
        body.setLatitude("Lat");
        body.setClientGuid("Client");
        assertEquals("City", body.getCity());
        assertEquals("Street", body.getStreet());
        assertEquals("AddrId", body.getAddressId());
        assertEquals("Long", body.getLongitude());
        assertEquals("Lat", body.getLatitude());
        assertEquals("Client", body.getClientGuid());
    }

    @Test
    void testAllArgsConstructor() {
        AddressUpdatedEventBody body = new AddressUpdatedEventBody("City", "Street", "AddrId", "Long", "Lat", "Client");
        assertEquals("City", body.getCity());
        assertEquals("Street", body.getStreet());
        assertEquals("AddrId", body.getAddressId());
        assertEquals("Long", body.getLongitude());
        assertEquals("Lat", body.getLatitude());
        assertEquals("Client", body.getClientGuid());
    }
}