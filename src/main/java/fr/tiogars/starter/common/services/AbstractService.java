package fr.tiogars.starter.common.services;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Generic base service for common operations (e.g., findById).
 * Subclasses provide the entity-to-model mapping via {@link #toModel(Object)}.
 */
public abstract class AbstractService<E, I, M> {
    protected final JpaRepository<E, I> repository;

    protected AbstractService(JpaRepository<E, I> repository) {
        this.repository = repository;
    }

    /**
     * Returns the model if found, or null otherwise.
     */
    public M findById(I id) {
        return repository.findById(id)
                .map(this::toModel)
                .orElse(null);
    }

    protected abstract M toModel(E entity);
}