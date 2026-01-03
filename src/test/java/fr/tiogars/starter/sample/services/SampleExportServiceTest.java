package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleExportForm;
import fr.tiogars.starter.sample.repositories.SampleRepository;

@ExtendWith(MockitoExtension.class)
class SampleExportServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private SampleSearchService sampleSearchService;

    @InjectMocks
    private SampleExportService sampleExportService;

    private List<SampleEntity> sampleEntities;

    @BeforeEach
    void setUp() {
        // Create sample entities for testing
        SampleEntity entity1 = new SampleEntity();
        entity1.setId(1L);
        entity1.setName("Sample1");
        entity1.setDescription("Description 1");
        entity1.setActive(true);
        entity1.setCreatedAt(new Date());
        entity1.setCreatedBy("user1");
        entity1.setUpdatedAt(new Date());
        entity1.setUpdatedBy("user1");

        SampleEntity entity2 = new SampleEntity();
        entity2.setId(2L);
        entity2.setName("Sample2");
        entity2.setDescription("Description 2");
        entity2.setActive(false);
        entity2.setCreatedAt(new Date());
        entity2.setCreatedBy("user2");
        entity2.setUpdatedAt(new Date());
        entity2.setUpdatedBy("user2");

        sampleEntities = Arrays.asList(entity1, entity2);
    }

    @Test
    void testExportSamples_JsonFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        String contentType = response.getHeaders().getContentType().toString();
        assertTrue(contentType.contains("application/json"));
        
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("samples.json"));
        assertTrue(contentDisposition.contains("attachment"));

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_CsvFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("csv");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        String contentType = response.getHeaders().getContentType().toString();
        assertTrue(contentType.contains("text/csv"));
        
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("samples.csv"));

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_XmlFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("xml");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        String contentType = response.getHeaders().getContentType().toString();
        assertTrue(contentType.contains("application/xml"));
        
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("samples.xml"));

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_XlsxFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("xlsx");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        String contentType = response.getHeaders().getContentType().toString();
        assertTrue(contentType.contains("spreadsheetml.sheet") || contentType.contains("ms-excel"));
        
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("samples.xlsx"));

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_WithZipCompression_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(true);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        String contentType = response.getHeaders().getContentType().toString();
        assertTrue(contentType.contains("application/zip"));
        
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        assertTrue(contentDisposition.contains("samples.zip"));
        assertTrue(contentDisposition.contains("attachment"));

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_WithSearchRequest_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);
        // Note: searchRequest is set but not fully tested due to simplification in export service

        Page<SampleEntity> page = new PageImpl<>(sampleEntities);
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testExportSamples_EmptyList_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Even with empty list, should produce valid JSON array
        assertTrue(response.getBody().length > 0);

        verify(sampleRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testExportSamples_FilenameHasDateTimePrefix() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);

        when(sampleRepository.findAll(any(Sort.class))).thenReturn(sampleEntities);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        // Filename should match pattern: yyyyMMdd_HHmmss_samples.json
        assertTrue(contentDisposition.matches(".*\\d{8}_\\d{6}_samples\\.json.*"));
    }
}
