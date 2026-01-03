package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleExportForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
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

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

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

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_CsvFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("csv");
        form.setZip(false);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

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

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_XmlFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("xml");
        form.setZip(false);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

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

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_XlsxFormat_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("xlsx");
        form.setZip(false);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

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

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_WithZipCompression_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(true);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

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

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_WithSearchRequest_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);
        
        SampleSearchRequest searchRequest = new SampleSearchRequest();
        searchRequest.setPage(0);
        searchRequest.setPageSize(100);
        form.setSearchRequest(searchRequest);

        SampleSearchResponse searchResponse = new SampleSearchResponse(
            sampleEntities.stream().map(this::toModel).toList(),
            sampleEntities.size()
        );
        
        when(sampleSearchService.search(any())).thenReturn(searchResponse);

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        
        verify(sampleSearchService, times(1)).search(any());
    }
    
    private Sample toModel(SampleEntity entity) {
        Sample model = new Sample();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        return model;
    }

    @Test
    void testExportSamples_EmptyList_Success() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.Collections.emptyList()));

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Even with empty list, should produce valid JSON array
        assertTrue(response.getBody().length > 0);

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }

    @Test
    void testExportSamples_FilenameHasDateTimePrefix() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(false);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        // Filename should match pattern: yyyyMMdd_HHmmss_samples.json
        assertTrue(contentDisposition.matches(".*\\d{8}_\\d{6}_samples\\.json.*"));
    }

    @Test
    void testExportSamples_ZipFileIsValidAndCanBeUnzipped() throws Exception {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        form.setFormat("json");
        form.setZip(true);

        when(sampleRepository.findAll(any(org.springframework.data.domain.PageRequest.class)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(sampleEntities));

        // Act
        ResponseEntity<byte[]> response = sampleExportService.exportSamples(form);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getBody());
        byte[] zipContent = response.getBody();
        assertTrue(zipContent.length > 0);

        // Verify the ZIP file can be properly opened and extracted
        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipContent);
             ZipInputStream zis = new ZipInputStream(bais)) {
            
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry, "ZIP file should contain at least one entry");
            
            // Verify the entry name matches expected pattern (timestamp_samples.json)
            String entryName = entry.getName();
            assertTrue(entryName.matches("\\d{8}_\\d{6}_samples\\.json"), 
                      "Entry name should match pattern yyyyMMdd_HHmmss_samples.json but was: " + entryName);
            
            // Read and verify the content can be extracted
            byte[] buffer = new byte[1024];
            int totalRead = 0;
            int bytesRead;
            while ((bytesRead = zis.read(buffer)) != -1) {
                totalRead += bytesRead;
            }
            
            assertTrue(totalRead > 0, "ZIP entry should contain data");
            
            // Verify the extracted content is valid JSON (starts with '[' for array)
            try (ByteArrayInputStream bais2 = new ByteArrayInputStream(zipContent);
                 ZipInputStream zis2 = new ZipInputStream(bais2)) {
                zis2.getNextEntry();
                byte[] contentBytes = zis2.readAllBytes();
                String jsonContent = new String(contentBytes);
                assertTrue(jsonContent.trim().startsWith("["), "Content should be a JSON array");
                assertTrue(jsonContent.trim().endsWith("]"), "Content should be a JSON array");
            }
            
            zis.closeEntry();
            
            // Verify there are no additional entries (should only be one file in the ZIP)
            assertNull(zis.getNextEntry(), "ZIP file should contain only one entry");
        }

        verify(sampleRepository, times(1)).findAll(any(org.springframework.data.domain.PageRequest.class));
    }
}
