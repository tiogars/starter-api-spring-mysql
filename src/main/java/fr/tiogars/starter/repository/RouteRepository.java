package fr.tiogars.starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.entities.RouteEntity;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

}
