package fr.tiogars.starter.repository.services;

import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.repositories.RepositoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RepositorySearchService {
    private final RepositoryRepository repositoryRepository;

    public RepositorySearchService(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    public List<Repository> search() {
        return repositoryRepository.findAll().stream().map(this::toModel).toList();
    }

    private Repository toModel(RepositoryEntity entity) {
        Repository model = new Repository();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
