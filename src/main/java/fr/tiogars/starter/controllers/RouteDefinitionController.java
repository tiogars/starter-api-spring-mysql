package fr.tiogars.starter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.models.Route;
import fr.tiogars.starter.repository.RouteRepository;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * use tag for rtkquery
 */
@RestController
@Tag(name = "route", description = "CRUD operations for Route entities")
public class RouteDefinitionController {

    @Autowired
    private RouteRepository routeRepository;

    @GetMapping("/routes")
    public List<Route> getAllRoute() {
        return routeRepository.findAll();
    }

    @PostMapping("/routes")
    public Route createRoute(@RequestBody Route route) {
        return routeRepository.save(route);
    }

    @GetMapping("/routes/{id}")
    public Route getRouteById(@PathVariable Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    @PutMapping("/routes/{id}")
    public Route updateRoute(@PathVariable Long id, @RequestBody Route route) {
        route.setId(id);
        return routeRepository.save(route);
    }

    @DeleteMapping("/routes/{id}")
    public void deleteRoute(@PathVariable Long id) {
        routeRepository.deleteById(id);
    }
}
