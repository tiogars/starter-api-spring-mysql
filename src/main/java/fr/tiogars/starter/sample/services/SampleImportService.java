package fr.tiogars.starter.sample.services;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleImportForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleImportReport;
import fr.tiogars.starter.sample.models.SampleImportReportItem;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolationException;

/**
 * Service for importing multiple samples in bulk.
 * Handles duplicate checking and provides detailed reporting for each sample.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
@Service
@Validated
public class SampleImportService {

    private final Logger logger = Logger.getLogger(SampleImportService.class.getName());
    private final SampleRepository sampleRepository;
    private final SampleCreateService sampleCreateService;

    public SampleImportService(SampleRepository sampleRepository, SampleCreateService sampleCreateService) {
        this.sampleRepository = sampleRepository;
        this.sampleCreateService = sampleCreateService;
    }

    /**
     * Import multiple samples and return a detailed report.
     * 
     * @param form The form containing samples to import
     * @return A report with import statistics and individual item results
     */
    public SampleImportReport importSamples(SampleImportForm form) {
        SampleImportReport report = new SampleImportReport();
        
        if (form.getSamples() == null || form.getSamples().isEmpty()) {
            report.setTotalProvided(0);
            report.setTotalCreated(0);
            report.setTotalSkipped(0);
            report.setMessage("No samples provided for import");
            report.setAlertLevel("warning");
            return report;
        }
        
        int totalProvided = form.getSamples().size();
        int totalCreated = 0;
        int totalSkipped = 0;
        
        logger.info("Starting import of " + totalProvided + " samples");
        
        for (SampleCreateForm sampleForm : form.getSamples()) {
            SampleImportReportItem item = importSingleSample(sampleForm);
            report.addItem(item);
            
            if (item.isCreated()) {
                totalCreated++;
            } else {
                totalSkipped++;
            }
        }
        
        report.setTotalProvided(totalProvided);
        report.setTotalCreated(totalCreated);
        report.setTotalSkipped(totalSkipped);
        
        // Set overall message and alert level
        if (totalCreated == totalProvided) {
            report.setMessage("Successfully imported " + totalCreated + " of " + totalProvided + " samples");
            report.setAlertLevel("success");
        } else if (totalCreated > 0) {
            report.setMessage("Imported " + totalCreated + " of " + totalProvided + " samples. " + totalSkipped + " skipped due to duplicates");
            report.setAlertLevel("info");
        } else {
            report.setMessage("No samples imported. All " + totalSkipped + " samples already exist");
            report.setAlertLevel("warning");
        }
        
        logger.info("Import completed: " + totalCreated + " created, " + totalSkipped + " skipped");
        
        return report;
    }

    /**
     * Import a single sample, checking for duplicates.
     * 
     * @param sampleForm The sample form to import
     * @return A report item with the result of the import
     */
    private SampleImportReportItem importSingleSample(SampleCreateForm sampleForm) {
        String name = sampleForm.getName();
        
        // Check if sample with same name already exists
        if (sampleRepository.findByName(name).isPresent()) {
            logger.info("Sample with name '" + name + "' already exists, skipping");
            return new SampleImportReportItem(
                name,
                false,
                "Sample with name '" + name + "' already exists",
                "info"
            );
        }
        
        // Try to create the sample
        try {
            Sample createdSample = sampleCreateService.create(sampleForm);
            logger.info("Sample '" + name + "' created successfully with ID: " + createdSample.getId());
            return new SampleImportReportItem(
                name,
                true,
                "Sample created successfully",
                "success"
            );
        } catch (ConstraintViolationException e) {
            logger.warning("Validation failed for sample '" + name + "': " + e.getMessage());
            return new SampleImportReportItem(
                name,
                false,
                "Validation failed: " + e.getMessage(),
                "error"
            );
        } catch (Exception e) {
            logger.severe("Failed to create sample '" + name + "': " + e.getMessage());
            return new SampleImportReportItem(
                name,
                false,
                "Failed to create sample: " + e.getMessage(),
                "error"
            );
        }
    }
}
