package fr.tiogars.starter.app.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.app.repositories.AppRepository;

@Service
public class AppService {
    private final AppRepository appRepository;
    public AppService(AppRepository appRepository) { this.appRepository = appRepository; }

    public Optional<AppEntity> findByName(String name) { return appRepository.findByName(name); }

    public AppEntity findOrCreateByName(String name) {
        return appRepository.findByName(name)
            .orElseGet(() -> {
                AppEntity entity = new AppEntity();
                entity.setName(name);
                return appRepository.save(entity);
            });
    }
}
