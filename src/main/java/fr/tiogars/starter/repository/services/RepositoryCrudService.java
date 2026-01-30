package fr.tiogars.starter.repository.services;

import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.repository.forms.RepositoryCreateForm;
import fr.tiogars.starter.repository.forms.RepositoryUpdateForm;
import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.repositories.RepositoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryCrudService {
    private final RepositoryRepository repositoryRepository;

    public RepositoryCrudService(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository;
    }

    public List<Repository> findAll() {
        return repositoryRepository.findAll().stream().map(this::toModel).toList();
    }

    public Optional<Repository> findById(Long id) {
        return repositoryRepository.findById(id).map(this::toModel);
    }

    @Transactional
    public Repository create(RepositoryCreateForm form) {
        RepositoryEntity entity = new RepositoryEntity();
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        return toModel(repositoryRepository.save(entity));
    }

    @Transactional
    public Optional<Repository> update(Long id, RepositoryUpdateForm form) {
        return repositoryRepository.findById(id).map(entity -> {
            entity.setName(form.getName());
            entity.setDescription(form.getDescription());
            return toModel(repositoryRepository.save(entity));
        });
    }

    @Transactional
    public void delete(Long id) {
        repositoryRepository.deleteById(id);
    }

    private Repository toModel(RepositoryEntity entity) {
        Repository model = new Repository();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
