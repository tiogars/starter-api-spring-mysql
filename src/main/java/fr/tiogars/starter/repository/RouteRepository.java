package fr.tiogars.starter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tiogars.starter.models.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {

}
