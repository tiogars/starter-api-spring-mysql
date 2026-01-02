package fr.tiogars.starter.sample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.sample.entities.SampleEntity;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
