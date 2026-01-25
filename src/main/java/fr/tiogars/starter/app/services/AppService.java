package fr.tiogars.starter.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.app.models.App;
import fr.tiogars.starter.app.repositories.AppRepository;

@Service
public class AppService {
    private final AppRepository appRepository;
    public AppService(AppRepository appRepository) { this.appRepository = appRepository; }

    public List<App> findAll() {
        return appRepository.findAll().stream().map(this::toModel).toList();
    }

    public Optional<App> findById(Long id) { 
        return appRepository.findById(id).map(this::toModel); 
    }

    public Optional<AppEntity> findByName(String name) { 
        return appRepository.findByName(name); 
    }

    public App create(App app) {
        AppEntity entity = new AppEntity();
        entity.setName(app.getName());
        entity.setVersion(app.getVersion());
        return toModel(appRepository.save(entity));
    }

    public void deleteById(Long id) { 
        appRepository.deleteById(id); 
    }

    public AppEntity findOrCreateByName(String name) {
        return appRepository.findByName(name)
            .orElseGet(() -> {
                AppEntity entity = new AppEntity();
                entity.setName(name);
                return appRepository.save(entity);
            });
    }

    private App toModel(AppEntity entity) {
        return new App(entity.getId(), entity.getName(), entity.getVersion());
    }
}
