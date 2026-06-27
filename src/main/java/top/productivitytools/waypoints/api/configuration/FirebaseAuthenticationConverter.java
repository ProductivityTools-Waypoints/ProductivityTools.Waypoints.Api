package top.productivitytools.waypoints.api.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FirebaseAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Set<String> ALLOWED_EMAILS = Set.of(
            "pwujczyk@gmail.com",
            "malgorzata.wujczyk@gmail.com"
    );

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        String email = jwt.getClaimAsString("email");
        if (email != null && ALLOWED_EMAILS.contains(email.toLowerCase())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ALLOWED_USER"));
        }

        // We can also extract default scopes if needed, but since we are locking down
        // the app to only these two users, ROLE_ALLOWED_USER is sufficient.
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
