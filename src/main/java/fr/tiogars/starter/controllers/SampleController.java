package fr.tiogars.starter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.forms.SampleCreateForm;
import fr.tiogars.starter.models.Sample;
import fr.tiogars.starter.service.SampleCreateService;
import fr.tiogars.starter.service.SampleCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "sample", description = "CRUD operations for Sample entities")
public class SampleController {

    private SampleCreateService sampleCreateService;

    private SampleCrudService sampleCrudService;

    public SampleController(SampleCreateService sampleCreateService, SampleCrudService sampleCrudService) {
        this.sampleCreateService = sampleCreateService;
        this.sampleCrudService = sampleCrudService;
    }

    @PostMapping("sample")
    public Sample createSample(@RequestBody SampleCreateForm form) {
        
        return this.sampleCreateService.create(form);
    }

    @GetMapping("sample/{id}")
    public Sample getSample(@PathVariable Long id) {
        return this.sampleCrudService.findById(id)
                .orElse(null);
    }

    @GetMapping("sample")
    public List<Sample> getAllSamples() {
        return this.sampleCrudService.findAll();
    }

    @DeleteMapping("sample/{id}")
    public void deleteSample(@PathVariable Long id) {
        this.sampleCrudService.deleteById(id);
    }
}
