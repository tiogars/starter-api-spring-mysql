package fr.tiogars.starter.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * Test configuration that provides mock OAuth2 client registrations.
 * This ensures OAuth2 auto-configuration doesn't fail during WebMvcTest.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Create a dummy client registration for testing
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("test")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .clientAuthenticationMethod(org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/test")
                .scope("read", "write")
                .authorizationUri("http://localhost:9000/oauth/authorize")
                .tokenUri("http://localhost:9000/oauth/token")
                .userInfoUri("http://localhost:9000/user")
                .userNameAttributeName("sub")
                .clientName("Test OAuth2 Provider")
                .build();
        
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}

