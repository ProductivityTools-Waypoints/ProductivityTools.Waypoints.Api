package top.productivitytools.waypoints.api.configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;

class FirebaseAuthenticationConverterTest {

    private final FirebaseAuthenticationConverter converter = new FirebaseAuthenticationConverter();

    @Test
    void convert_withAllowedEmailPwujczyk_grantsRole() {
        Jwt jwt = createJwtWithEmail("pwujczyk@gmail.com");
        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertTrue(token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALLOWED_USER")));
    }

    @Test
    void convert_withAllowedEmailMalgorzata_grantsRole() {
        Jwt jwt = createJwtWithEmail("malgorzata.wujczyk@gmail.com");
        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertTrue(token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALLOWED_USER")));
    }

    @Test
    void convert_withAllowedEmailCaseInsensitive_grantsRole() {
        Jwt jwt = createJwtWithEmail("PwUjCzYk@GmAiL.cOm");
        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertTrue(token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALLOWED_USER")));
    }

    @Test
    void convert_withOtherEmail_doesNotGrantRole() {
        Jwt jwt = createJwtWithEmail("other@gmail.com");
        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertFalse(token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALLOWED_USER")));
    }

    @Test
    void convert_withNullEmail_doesNotGrantRole() {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .subject("user123")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertFalse(token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALLOWED_USER")));
    }

    private Jwt createJwtWithEmail(String email) {
        return Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .subject("user123")
                .claim("email", email)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }
}
