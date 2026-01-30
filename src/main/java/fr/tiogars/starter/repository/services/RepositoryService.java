package fr.tiogars.starter.repository.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.repositories.RepositoryRepository;

@Service
public class RepositoryService {
    private final RepositoryRepository repositoryRepository;
    public RepositoryService(RepositoryRepository repositoryRepository) { this.repositoryRepository = repositoryRepository; }

    public Optional<RepositoryEntity> findByName(String name) {
        return repositoryRepository.findByName(name);
    }

    public RepositoryEntity findOrCreateByName(String name) {
        return repositoryRepository.findByName(name)
            .orElseGet(() -> {
                RepositoryEntity entity = new RepositoryEntity();
                entity.setName(name);
                return repositoryRepository.save(entity);
            });
    }
}
