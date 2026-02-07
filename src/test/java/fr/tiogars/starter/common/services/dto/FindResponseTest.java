package fr.tiogars.starter.common.services.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class FindResponseTest {

    @Test
    void constructor_nullData_createsEmptyResponse() {
        FindResponse<String> response = new FindResponse<>(null);

        assertNotNull(response.getData());
        assertEquals(0, response.getData().size());
        assertEquals(0, response.getCount());
    }
}
