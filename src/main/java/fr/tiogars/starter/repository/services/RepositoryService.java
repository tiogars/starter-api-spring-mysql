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

    public List<Repository> findAll() {
        return repositoryRepository.findAll().stream().map(this::toModel).toList();
    }

    public Optional<Repository> findById(Long id) { 
        return repositoryRepository.findById(id).map(this::toModel); 
    }

    public Optional<RepositoryEntity> findByName(String name) { 
        return repositoryRepository.findByName(name); 
    }

    public Repository create(Repository repository) {
        RepositoryEntity entity = new RepositoryEntity();
        entity.setName(repository.getName());
        entity.setUrl(repository.getUrl());
        return toModel(repositoryRepository.save(entity));
    }

    public void deleteById(Long id) { 
        repositoryRepository.deleteById(id); 
    }

    public RepositoryEntity findOrCreateByName(String name) {
        return repositoryRepository.findByName(name)
            .orElseGet(() -> {
                RepositoryEntity entity = new RepositoryEntity();
                entity.setName(name);
                return repositoryRepository.save(entity);
            });
    }

    private Repository toModel(RepositoryEntity entity) {
        return new Repository(entity.getId(), entity.getName(), entity.getUrl());
    }
}
