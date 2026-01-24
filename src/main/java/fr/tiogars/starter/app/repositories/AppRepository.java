package fr.tiogars.starter.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.app.entities.AppEntity;

public interface AppRepository extends JpaRepository<AppEntity, Long> {
    Optional<AppEntity> findByName(String name);
}
