package fr.tiogars.starter.app.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppEntityTest {
    @Test
    void gettersAndSettersWork() {
        AppEntity e = new AppEntity();
        e.setId(1L);
        e.setName("n");
        e.setVersion("v");
        assertEquals(1L, e.getId());
        assertEquals("n", e.getName());
        assertEquals("v", e.getVersion());
    }
}
