package fr.tiogars.starter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Value("${keycloak.auth-server-url:}")
    String authServerUrl;

    @Value("${keycloak.realm:}")
    String realm;

    private final ObjectProvider<BuildProperties> buildPropertiesProvider;

    private static final String OAUTH_SCHEME_NAME = "oauth2";

    public OpenApiConfig(ObjectProvider<BuildProperties> buildPropertiesProvider) {
        this.buildPropertiesProvider = buildPropertiesProvider;
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info().title("Starter API")
                        .description("Starter API for Spring Boot projects with MySQL and Keycloak.")
                        .version(resolveVersion()));
        
        // Only add OAuth2 security scheme if Keycloak is properly configured
        if (isKeycloakConfigured()) {
            openAPI.components(new Components()
                    .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                    .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
        }
        
        return openAPI;
    }
    
    private boolean isKeycloakConfigured() {
        return authServerUrl != null && !authServerUrl.isEmpty() 
                && realm != null && !realm.isEmpty();
    }

    private String resolveVersion() {
        BuildProperties buildProperties = buildPropertiesProvider.getIfAvailable();
        return buildProperties != null ? buildProperties.getVersion() : "unknown";
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow flow = createAuthorizationCodeFlow();
        return new OAuthFlows().implicit(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        if (!isKeycloakConfigured()) {
            return new OAuthFlow();
        }
        return new OAuthFlow()
                .authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth")
                .tokenUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
        // Adding scopes is optional and depends on your specific requirements
        // .scopes(new Scopes().addString("read_access", "read data")
        // .addString("write_access", "modify data"))
        ;
    }
}
