package fr.tiogars.starter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité pour l'application Spring Boot.
 * Cette classe configure les règles de sécurité pour les différentes URL de
 * l'application.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configure les règles de sécurité pour les différentes URL de l'application.
     * Cette méthode définit quelles URL sont accessibles sans authentification et
     * quelles URL nécessitent une authentification.
     * Les URL liées à Swagger UI et à la documentation API sont accessibles sans
     * authentification, tandis que toutes les autres URL nécessitent une
     * authentification.
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
     * @param http L'objet HttpSecurity utilisé pour configurer les règles de
     *             sécurité
     * @return La chaîne de filtres de sécurité configurée
     * @throws SecurityException en cas d'erreur lors de la configuration de la
     *                           sécurité
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws SecurityException {
        http.authorizeHttpRequests(authorize -> authorize
                // Permet d'accéder à Swagger UI et à la documentation API sans authentification
                .requestMatchers(
                        "/swagger-ui/**", // Permet d'accéder à Swagger UI
                        "/api-docs/**", // Permet d'accéder à la documentation API générée par Springdoc OpenAPI
                        "/v3/api-docs/**", // Permet d'accéder à la documentation API générée par Springdoc OpenAPI
                                           // (avec le préfixe /v3)
                        "/swagger-ui.html" // Permet d'accéder à Swagger UI (ancienne URL pour les versions plus
                                           // anciennes de Springdoc OpenAPI
                )
                .permitAll()
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated())
                // Utilise l'authentification HTTP Basic et le formulaire de login par défaut de
                // Spring Security
                // .httpBasic(Customizer.withDefaults())
                // Active le formulaire de login par défaut de Spring Security
                // .formLogin(Customizer.withDefaults())
                // Use Oath2 login with default configuration (redirect to Keycloak for
                // authentication)
                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
