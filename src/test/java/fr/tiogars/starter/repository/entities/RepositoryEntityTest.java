package fr.tiogars.starter.repository.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RepositoryEntityTest {
    @Test
    void gettersAndSettersWork() {
        RepositoryEntity e = new RepositoryEntity();
        e.setId(1L);
        e.setName("n");
        e.setUrl("u");
        assertEquals(1L, e.getId());
        assertEquals("n", e.getName());
        assertEquals("u", e.getUrl());
    }
}
