package fr.tiogars.starter.common.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Generic base service for read-only find operations to respect SRP.
 * Subclasses provide the entity-to-model mapping via {@link #toModel(Object)}.
 */
public abstract class AbstractFindService<E, I, M> {
    private final JpaRepository<E, I> repository;

    protected AbstractFindService(JpaRepository<E, I> repository) {
        this.repository = repository;
    }

    public List<M> findAll() {
        return repository.findAll().stream()
                .map(this::toModel)
                .toList();
    }

    public Optional<M> findById(I id) {
        return repository.findById(id).map(this::toModel);
    }

    protected abstract M toModel(E entity);
}
