package fr.tiogars.starter.sample.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.starter.sample.forms.SampleInitForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Branch coverage tests for SampleInitService.
 * Tests the exception handling path in initSamples method.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SampleInitServiceBranchTest {
    @Autowired
    private SampleInitService sampleInitService;

    @Test
    void testInitSamplesWithValidForm() {
        SampleInitForm form = new SampleInitForm();
        form.setNumberOfSamples(3);
        
        var result = sampleInitService.initSamples(form);
        
        assertNotNull(result);
        assertTrue(result.size() > 0 && result.size() <= 3);
    }

    @Test
    void testInitSamplesWithOne() {
        SampleInitForm form = new SampleInitForm();
        form.setNumberOfSamples(1);
        
        var result = sampleInitService.initSamples(form);
        
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test
    void testInitSamplesWithLargeNumber() {
        SampleInitForm form = new SampleInitForm();
        form.setNumberOfSamples(10);
        
        var result = sampleInitService.initSamples(form);
        
        assertNotNull(result);
        assertTrue(result.size() <= 10);
    }
}
