package fr.tiogars.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration for the Spring Boot application.
 * This class defines access rules for the application's URLs.
 */
@Configuration
public class SecurityConfig {

    @Value("${keycloak.auth-server-url:}")
    String authServerUrl;

    @Value("${keycloak.realm:}")
    String realm;

        /**
         * Configures access rules for the application's URLs.
         * Swagger UI and API docs are always public. If Keycloak is configured,
         * all other endpoints require authentication; otherwise everything is open
         * for local development.
         *
         * @see <a href=
         *      "https://www.baeldung.com/java-spring-security-permit-swagger-ui">Baeldung
         *      - Spring Security Permit Swagger UI</a>
         *
         * @see <a href=
         *      "https://github.com/tiogars/starter-api-spring-mysql/commit/4719ce20dced73d54954ed65d545a54bba104807">Commit
         *      - Add security configuration to permit access to Swagger UI and API
         *      documentation</a>
         *
         * @param http The HttpSecurity instance used to configure security rules
         * @return The configured security filter chain
         * @throws SecurityException if an error occurs while configuring security
         */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws SecurityException {
        http.cors(Customizer.withDefaults());

        if (isKeycloakConfigured()) {
                http.oauth2Login(Customizer.withDefaults())
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                    .authorizeHttpRequests(authorize -> authorize
                        // Allow Swagger UI and API docs without authentication
                        .requestMatchers(
                            "/swagger-ui/**",
                            "/api-docs/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                        )
                        .permitAll()
                        .anyRequest().authenticated());
        } else {
            http.authorizeHttpRequests(authorize -> authorize
                // Allow Swagger UI and API docs without authentication
                .requestMatchers(
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                )
                .permitAll()
                .anyRequest().permitAll());
        }

        return http.build();
    }

    /**
     * Configures CORS to allow cross-origin requests with OAuth2/Keycloak support.
     * In development, restricts origins to localhost.
     * In production, this configuration should be limited to approved origins.
     *
     * @return The CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Use explicit origin patterns because credentials are enabled.
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setAllowCredentials(true); // Required for OAuth2/JWT token handling
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private boolean isKeycloakConfigured() {
        return authServerUrl != null && !authServerUrl.isBlank()
                && realm != null && !realm.isBlank();
    }
}
