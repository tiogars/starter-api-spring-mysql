package fr.tiogars.starter.sample.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.tiogars.starter.sample.entities.SampleEntity;

public interface SampleRepository extends JpaRepository<SampleEntity, Long>, JpaSpecificationExecutor<SampleEntity> {
    
    /**
     * Find a sample by its name
     * @param name The name of the sample
     * @return Optional containing the sample if found
     */
    Optional<SampleEntity> findByName(String name);
}
