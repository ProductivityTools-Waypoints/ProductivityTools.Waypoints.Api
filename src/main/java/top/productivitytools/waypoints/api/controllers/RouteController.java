package top.productivitytools.waypoints.api.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import com.google.cloud.spring.data.firestore.FirestoreTemplate;
import top.productivitytools.waypoints.api.models.RouteInput;
import top.productivitytools.waypoints.api.models.Route;
import top.productivitytools.waypoints.api.models.Point;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import java.util.ArrayList;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class RouteController {
    private final FirestoreTemplate firestoreTemplate;

    @MutationMapping
    public Route AddRoute(@Argument("route") RouteInput routeInput) {
        RouteInput saved = this.firestoreTemplate.save(routeInput).block();
        Route route = new Route();
        if (saved != null) {
            route.setId(saved.getId());
            route.setName(saved.getName());
            if (saved.getPoints() != null) {
                Point[] points = new Point[saved.getPoints().length];
                for (int i = 0; i < saved.getPoints().length; i++) {
                    points[i] = new Point(saved.getPoints()[i].getName());
                }
                route.setPoints(points);
            }
        }
        return route;
    }

    @QueryMapping   
    public List<Route> getRoutes() {
        //return new ArrayList<Route>() {{ add(new Route("1", "Route 1")); add(new Route("2", "Route 2")); }};
        List<Route> routes = this.firestoreTemplate.findAll(Route.class).collectList().block();
        return routes;
    }

    @QueryMapping   
    public Route getRoute(@Argument("id") String id) {
        Route route = this.firestoreTemplate.findById(Mono.just(id), Route.class).block();
        return route;
    }

}
