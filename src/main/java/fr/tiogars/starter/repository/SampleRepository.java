package fr.tiogars.starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.entities.SampleEntity;

public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
