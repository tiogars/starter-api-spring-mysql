package fr.tiogars.starter.sample.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleInitForm;
import fr.tiogars.starter.sample.models.Sample;
import jakarta.validation.Valid;

/**
 * Service for initializing multiple samples with mock data.
 * Generates samples with random names and descriptions for testing and development purposes.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
@Service
@Validated
public class SampleInitService {

    private final Logger logger = Logger.getLogger(SampleInitService.class.getName());
    private final SampleCreateService sampleCreateService;
    private final Random random = new Random();

    // Mock data for generating samples
    private static final String[] ADJECTIVES = {
        "Quick", "Lazy", "Happy", "Sad", "Bright", "Dark", "Fast", "Slow", "Big", "Small"
    };
    
    private static final String[] NOUNS = {
        "Fox", "Dog", "Cat", "Bird", "Fish", "Lion", "Tiger", "Bear", "Wolf", "Eagle"
    };

    public SampleInitService(SampleCreateService sampleCreateService) {
        this.sampleCreateService = sampleCreateService;
    }

    /**
     * Initialize samples with mock data.
     * 
     * @param form The form containing the number of samples to create
     * @return A list of created samples
     */
    public List<Sample> initSamples(@Valid SampleInitForm form) {
        int numberOfSamples = form.getNumberOfSamples();
        logger.info("Initializing " + numberOfSamples + " samples with mock data");
        
        List<Sample> createdSamples = new ArrayList<>();
        
        for (int i = 0; i < numberOfSamples; i++) {
            SampleCreateForm createForm = generateMockSampleForm(i);
            try {
                Sample createdSample = sampleCreateService.create(createForm);
                createdSamples.add(createdSample);
                logger.info("Created sample " + (i + 1) + "/" + numberOfSamples + ": " + createdSample.getName());
            } catch (Exception e) {
                logger.warning("Failed to create sample " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        logger.info("Successfully created " + createdSamples.size() + " of " + numberOfSamples + " samples");
        return createdSamples;
    }

    /**
     * Generate a mock sample form with random data.
     * 
     * @param index The index of the sample being created
     * @return A SampleCreateForm with mock data
     */
    private SampleCreateForm generateMockSampleForm(int index) {
        SampleCreateForm form = new SampleCreateForm();
        
        // Generate a random name (max 10 characters to comply with validation)
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        String name = adjective.substring(0, Math.min(3, adjective.length())) + 
                      noun.substring(0, Math.min(3, noun.length())) + 
                      (index + 1);
        
        // Ensure name doesn't exceed 10 characters
        if (name.length() > 10) {
            name = name.substring(0, 10);
        }
        
        form.setName(name);
        form.setDescription("Mock sample generated with " + adjective.toLowerCase() + " " + noun.toLowerCase());
        form.setActive(random.nextBoolean());
        
        return form;
    }
}
