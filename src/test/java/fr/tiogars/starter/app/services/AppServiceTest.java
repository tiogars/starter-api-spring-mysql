package fr.tiogars.starter.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.app.repositories.AppRepository;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

    @Mock private AppRepository appRepository;
    @InjectMocks private AppService appService;

    @Test
    void findOrCreateByName_returnsExisting() {
        AppEntity e = new AppEntity(); e.setId(1L); e.setName("a");
        when(appRepository.findByName("a")).thenReturn(Optional.of(e));
        assertSame(e, appService.findOrCreateByName("a"));
    }

    @Test
    void findOrCreateByName_createsWhenMissing() {
        when(appRepository.findByName("a")).thenReturn(Optional.empty());
        when(appRepository.save(any(AppEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        AppEntity created = appService.findOrCreateByName("a");
        assertEquals("a", created.getName());
    }

    @Test
    void findByName_passThrough() {
        AppEntity e = new AppEntity();
        when(appRepository.findByName("a")).thenReturn(Optional.of(e));
        assertTrue(appService.findByName("a").isPresent());
    }
}
