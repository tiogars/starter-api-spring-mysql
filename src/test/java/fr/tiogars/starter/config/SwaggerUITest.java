package fr.tiogars.starter.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration test to verify Swagger UI endpoints are accessible.
 * This test ensures that the SpringDoc OpenAPI configuration is correctly loaded
 * and that Swagger UI is available at expected paths.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SwaggerUITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAccessSwaggerUIIndexPage() throws Exception {
        // Verify that Swagger UI is accessible at /swagger-ui/index.html
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAccessSwaggerUIHtmlPage() throws Exception {
        // Verify that Swagger UI is also accessible at /swagger-ui.html
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void shouldAccessApiDocs() throws Exception {
        // Verify that API docs endpoint is accessible
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());
    }
}
