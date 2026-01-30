package fr.tiogars.starter.sample.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVWriter;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleExportForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.repositories.SampleRepository;

/**
 * Service for exporting samples in various formats.
 * Supports XLSX, CSV, XML, and JSON formats with optional ZIP compression.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
@Service
@Validated
public class SampleExportService {

    private static final Logger logger = LoggerFactory.getLogger(SampleExportService.class);
    private final SampleRepository sampleRepository;
    private final SampleSearchService sampleSearchService;

    public SampleExportService(SampleRepository sampleRepository, SampleSearchService sampleSearchService) {
        this.sampleRepository = sampleRepository;
        this.sampleSearchService = sampleSearchService;
    }

    /**
     * Export samples based on the provided form configuration.
     * 
     * @param form The export form containing format, zip option, and optional filters
     * @return ResponseEntity with the exported file as byte array
     */
    public ResponseEntity<byte[]> exportSamples(SampleExportForm form) {
        try {
            String format = (form != null && form.getFormat() != null) ? form.getFormat() : "unknown";
            logger.info("Starting export in format: {}", format);

            // Fetch samples based on search criteria
            List<Sample> samples = fetchSamples(form != null ? form.getSearchRequest() : null);

            logger.info("Exporting {} samples", samples.size());

            // Generate filename with datetime prefix
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String baseFilename = timestamp + "_samples." + format;

            // Generate file content
            byte[] fileContent = generateFileContent(samples, format);

            // Apply ZIP compression if requested
            byte[] finalContent;
            String filename;
            String contentType;

            boolean zip = form != null && form.isZip();
            if (zip) {
                finalContent = zipContent(fileContent, baseFilename);
                filename = timestamp + "_samples.zip";
                contentType = "application/zip";
            } else {
                finalContent = fileContent;
                filename = baseFilename;
                contentType = getContentType(format);
            }

            // Build response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(finalContent.length);

            logger.info("Export completed successfully: {}", filename);

            return new ResponseEntity<>(finalContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            String format = (form != null && form.getFormat() != null) ? form.getFormat() : "unknown";
            String errorMsg = String.format("Export failed for format: %s. Exception: %s", format, e.getMessage() != null ? e.getMessage() : e.toString());
            logger.error(errorMsg, e);
            throw new SampleExportException(errorMsg, e);
        }
    }

    /**
     * Custom exception for sample export errors.
     */
    public static class SampleExportException extends RuntimeException {
        public SampleExportException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Fetch samples based on search criteria or all samples if no criteria provided.
     * Note: Exports are limited to 100,000 records to prevent memory issues.
     */
    private List<Sample> fetchSamples(SampleSearchRequest searchRequest) {
        if (searchRequest != null) {
            // Create a copy to avoid modifying the input parameter
            SampleSearchRequest exportRequest = new SampleSearchRequest();
            exportRequest.setPage(0);
            exportRequest.setSortModel(searchRequest.getSortModel());
            exportRequest.setFilterModel(searchRequest.getFilterModel());
            
            // Use default of 10,000 if pageSize not set or invalid, max 100,000 for memory safety
            int requestedPageSize = searchRequest.getPageSize() > 0 ? searchRequest.getPageSize() : 10000;
            exportRequest.setPageSize(Math.min(requestedPageSize, 100000));
            
            var searchResponse = sampleSearchService.search(exportRequest);
            return searchResponse.getRows();
        } else {
            // Export all samples (limited to first 100,000 for memory safety)
            List<SampleEntity> entities = sampleRepository.findAll(
                PageRequest.of(0, 100000, Sort.by(Sort.Direction.ASC, "id"))
            ).getContent();
            return entities.stream().map(this::toModel).toList();
        }
    }

    /**
     * Generate file content based on format.
     */
    private byte[] generateFileContent(List<Sample> samples, String format) throws IOException {
        try {
            switch (format.toLowerCase()) {
                case "xlsx":
                    return generateXlsx(samples);
                case "csv":
                    return generateCsv(samples);
                case "xml":
                    return generateXml(samples);
                case "json":
                    return generateJson(samples);
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }
        } catch (IOException e) {
            logger.error("Failed to generate file content for format: {}", format, e);
            throw new SampleExportException("Failed to generate file content for format: " + format, e);
        }
    }

    /**
     * Generate XLSX file content.
     */
    private byte[] generateXlsx(List<Sample> samples) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Samples");
            
            // Create header row with bold font
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            String[] headers = {"ID", "Name", "Description", "Active", "Created At", "Created By", "Updated At", "Updated By"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Add data rows
            int rowNum = 1;
            for (Sample sample : samples) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sample.getId() != null ? sample.getId() : 0);
                row.createCell(1).setCellValue(sample.getName() != null ? sample.getName() : "");
                row.createCell(2).setCellValue(sample.getDescription() != null ? sample.getDescription() : "");
                row.createCell(3).setCellValue(sample.isActive());
                row.createCell(4).setCellValue(sample.getCreatedAt() != null ? sample.getCreatedAt().toString() : "");
                row.createCell(5).setCellValue(sample.getCreatedBy() != null ? sample.getCreatedBy() : "");
                row.createCell(6).setCellValue(sample.getUpdatedAt() != null ? sample.getUpdatedAt().toString() : "");
                row.createCell(7).setCellValue(sample.getUpdatedBy() != null ? sample.getUpdatedBy() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Generate CSV file content.
     */
    private byte[] generateCsv(List<Sample> samples) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out);
             CSVWriter csvWriter = new CSVWriter(writer)) {
            
            // Write header
            String[] headers = {"ID", "Name", "Description", "Active", "Created At", "Created By", "Updated At", "Updated By"};
            csvWriter.writeNext(headers);
            
            // Write data rows
            for (Sample sample : samples) {
                String[] row = {
                    sample.getId() != null ? sample.getId().toString() : "",
                    sample.getName() != null ? sample.getName() : "",
                    sample.getDescription() != null ? sample.getDescription() : "",
                    String.valueOf(sample.isActive()),
                    sample.getCreatedAt() != null ? sample.getCreatedAt().toString() : "",
                    sample.getCreatedBy() != null ? sample.getCreatedBy() : "",
                    sample.getUpdatedAt() != null ? sample.getUpdatedAt().toString() : "",
                    sample.getUpdatedBy() != null ? sample.getUpdatedBy() : ""
                };
                csvWriter.writeNext(row);
            }
            
            csvWriter.flush();
            return out.toByteArray();
        }
    }

    /**
     * Generate XML file content.
     */
    private byte[] generateXml(List<Sample> samples) throws IOException {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Wrap samples in a root element
            SamplesWrapper wrapper = new SamplesWrapper(samples);
            return xmlMapper.writeValueAsBytes(wrapper);
        } catch (Exception e) {
            logger.error("Failed to generate XML content", e);
            throw new SampleExportException("Failed to generate XML content", e);
        }
    }

    /**
     * Generate JSON file content.
     */
    private byte[] generateJson(List<Sample> samples) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return objectMapper.writeValueAsBytes(samples);
        } catch (Exception e) {
            logger.error("Failed to generate JSON content", e);
            throw new SampleExportException("Failed to generate JSON content", e);
        }
    }

    /**
     * Compress content into a ZIP file.
     */
    private byte[] zipContent(byte[] content, String filename) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(out)) {

            ZipEntry zipEntry = new ZipEntry(filename);
            zipOut.putNextEntry(zipEntry);
            zipOut.write(content);
            zipOut.closeEntry();
            zipOut.finish();

            return out.toByteArray();
        } catch (IOException e) {
            logger.error("Failed to zip content for filename: {}", filename, e);
            throw new SampleExportException("Failed to zip content for filename: " + filename, e);
        }
    }

    /**
     * Get content type based on format.
     */
    private String getContentType(String format) {
        switch (format.toLowerCase()) {
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv":
                return "text/csv";
            case "xml":
                return "application/xml";
            case "json":
                return "application/json";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Convert SampleEntity to Sample model.
     */
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

    /**
     * Wrapper class for XML serialization.
     */
    private static class SamplesWrapper {
        private final List<Sample> samples;

        public SamplesWrapper(List<Sample> samples) {
            this.samples = samples;
        }

        public List<Sample> getSamples() {
            return samples;
        }
    }
}
