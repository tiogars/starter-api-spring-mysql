package fr.tiogars.starter.sample.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.tiogars.starter.sample.entities.SampleTagEntity;

/**
 * Repository for SampleTag entities.
 */
@Repository
public interface SampleTagRepository extends JpaRepository<SampleTagEntity, Long> {
    /**
     * Find a tag by name
     * @param name the tag name
     * @return the tag entity
     */
    Optional<SampleTagEntity> findByName(String name);

    /**
     * Find all tags by name list
     * @param names the list of tag names
     * @return the list of tag entities
     */
    List<SampleTagEntity> findByNameIn(List<String> names);
}
