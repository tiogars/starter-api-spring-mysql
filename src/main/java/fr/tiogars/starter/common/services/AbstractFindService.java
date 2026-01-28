package fr.tiogars.starter.common.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.common.services.dto.FindResponse;

/**
 * Generic base service for read-only find operations to respect SRP.
 * Subclasses provide the entity-to-model mapping via {@link #toModel(Object)}.
 */

public abstract class AbstractFindService<E, I, M> extends AbstractService<E, I, M> {

    protected AbstractFindService(JpaRepository<E, I> repository) {
        super(repository);
    }


    /**
     * Returns a FindResponse containing all models, for API-friendly exposure.
     */
    public FindResponse<M> findAll() {
        List<M> models = repository.findAll().stream()
                .map(this::toModel)
                .toList();
        return new FindResponse<>(models);
    }


    // Note: findById(I id) is now defined in AbstractService and inherited here
    // to centralize common lookup logic and allow reuse across different service types.
    protected abstract M toModel(E entity);
}
