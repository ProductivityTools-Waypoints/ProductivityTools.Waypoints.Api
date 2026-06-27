package top.productivitytools.waypoints.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import reactor.core.publisher.Flux;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class GraphQLSecurityIntegrationTest {

    @Autowired
    private HttpGraphQlTester graphQlTester;

    @MockitoBean
    private FirestoreTemplate firestoreTemplate;

    @Test
    void helloQuery_isPublic() {
        String document = "query { helloQuery }";
        this.graphQlTester.document(document)
                .execute()
                .errors()
                .verify() // verifies no errors
                .path("helloQuery")
                .entity(String.class)
                .isEqualTo("Hello World");
    }

    @Test
    void getRoutes_anonymous_isDenied() {
        String document = "query { getRoutes { id name } }";
        this.graphQlTester.document(document)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assert !errors.isEmpty();
                    assert errors.stream().anyMatch(err -> 
                        err.getMessage().contains("Access is denied") || 
                        "FORBIDDEN".equals(err.getErrorType().toString()) ||
                        "UNAUTHORIZED".equals(err.getErrorType().toString())
                    );
                });
    }

    @Test
    @WithMockUser(roles = "ALLOWED_USER")
    void getRoutes_authorized_succeeds() {
        // Mock FirestoreTemplate.findAll to return empty list
        when(firestoreTemplate.findAll(any())).thenReturn(Flux.empty());

        String document = "query { getRoutes { id name } }";
        this.graphQlTester.document(document)
                .execute()
                .errors()
                .verify() // no errors
                .path("getRoutes")
                .entityList(Object.class)
                .hasSize(0);
    }
}
