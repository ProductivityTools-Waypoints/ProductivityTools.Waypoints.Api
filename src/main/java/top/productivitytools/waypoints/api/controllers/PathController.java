package top.productivitytools.waypoints.api.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import top.productivitytools.waypoints.api.models.Path;

@Controller
@RequiredArgsConstructor
public class PathController {
    private final FirestoreTemplate firestoreTemplate;

    @MutationMapping
    public String AddPath(@Argument("name") String name) {
        Path path = new Path();
        path.setName(name);
        this.firestoreTemplate.save(path).block();
        return "Path " + name + " saved to Firestore";
    }
}

