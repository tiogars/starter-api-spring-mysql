package fr.tiogars.starter.route.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.route.entities.RouteEntity;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

}
