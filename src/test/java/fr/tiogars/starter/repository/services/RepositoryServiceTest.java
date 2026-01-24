package fr.tiogars.starter.repository.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.repository.repositories.RepositoryRepository;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock private RepositoryRepository repositoryRepository;
    @InjectMocks private RepositoryService repositoryService;

    @Test
    void findOrCreateByName_returnsExisting() {
        RepositoryEntity e = new RepositoryEntity(); e.setId(1L); e.setName("r");
        when(repositoryRepository.findByName("r")).thenReturn(Optional.of(e));
        assertSame(e, repositoryService.findOrCreateByName("r"));
    }

    @Test
    void findOrCreateByName_createsWhenMissing() {
        when(repositoryRepository.findByName("r")).thenReturn(Optional.empty());
        when(repositoryRepository.save(any(RepositoryEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        RepositoryEntity created = repositoryService.findOrCreateByName("r");
        assertEquals("r", created.getName());
    }

    @Test
    void findByName_passThrough() {
        RepositoryEntity e = new RepositoryEntity();
        when(repositoryRepository.findByName("r")).thenReturn(Optional.of(e));
        assertTrue(repositoryService.findByName("r").isPresent());
    }
}
