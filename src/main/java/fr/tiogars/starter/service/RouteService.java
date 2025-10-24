package fr.tiogars.starter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.entities.RouteEntity;
import fr.tiogars.starter.forms.RouteCreateForm;
import fr.tiogars.starter.forms.RouteUpdateForm;
import fr.tiogars.starter.models.Route;
import fr.tiogars.starter.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> findAll() {
        return routeRepository.findAll().stream()
                .map(this::toModel)
                .toList();
    }

    public Route findById(Long id) {
        return routeRepository.findById(id).map(this::toModel).orElse(null);
    }

    public Route save(RouteCreateForm route) {
        return toModel(routeRepository.save(toEntity(route)));
    }

    public Route update(RouteUpdateForm form) {
        return toModel(routeRepository.save(toEntity(form)));
    }

    public void deleteById(Long id) {
        routeRepository.deleteById(id);
    }

    private RouteEntity toEntity(RouteCreateForm route) {
        RouteEntity entity = new RouteEntity();
        entity.setName(route.getName());
        entity.setPath(route.getPath());
        return entity;
    }

    private RouteEntity toEntity(RouteUpdateForm form) {
        RouteEntity entity = new RouteEntity();
        entity.setId(form.getId());
        entity.setName(form.getName());
        entity.setPath(form.getPath());
        return entity;
    }

    private Route toModel(RouteEntity entity) {
        Route model = new Route();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPath(entity.getPath());
        return model;
    }
}
