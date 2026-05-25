package top.productivitytools.waypoints.api.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import top.productivitytools.waypoints.api.models.Route;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class RouteController {
    private final FirestoreTemplate firestoreTemplate;

    @MutationMapping
    public String AddRoute(@Argument("name") String name) {
        Route route = new Route();
        route.setId(name);
        route.setName(name);
        this.firestoreTemplate.save(route).block();
        return "Route " + name + " saved to Firestore";
    }

    @QueryMapping   
    public List<Route> getRoutes() {
        //return new ArrayList<Route>() {{ add(new Route("1", "Route 1")); add(new Route("2", "Route 2")); }};
        List<Route> routes = this.firestoreTemplate.findAll(Route.class).collectList().block();
        return routes;
    }

    @QueryMapping   
    public Route getRoute(@Argument("id") String id) {
        Route route = this.firestoreTemplate.findById(id, Route.class).block();
        return route;
    }

}
