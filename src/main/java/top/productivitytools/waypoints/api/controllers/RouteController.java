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
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.ArrayList;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ALLOWED_USER')")
public class RouteController {
    private final FirestoreTemplate firestoreTemplate;

    // @MutationMapping
    // public Route AddRoute(@Argument("route") RouteInput routeInput) {
    //     RouteInput saved = this.firestoreTemplate.save(routeInput).block();
    //     Route route = new Route();
    //     if (saved != null) {
    //         route.setId(saved.getId());
    //         route.setName(saved.getName());
    //         route.setDirection(saved.getDirection());
    //         if (saved.getPoints() != null) {
    //             List<Point> points = new ArrayList<>();
    //             for (top.productivitytools.waypoints.api.models.PointInput pi : saved.getPoints()) {
    //                 points.add(new Point(pi.getName(), pi.getOdometer(), pi.getDistance()));
    //             }
    //             route.setPoints(points);
    //         }
    //     }
    //     return route;
    // }

    @MutationMapping
    public Route SaveRoute(@Argument("route") RouteInput routeInput) {
        String oldId = routeInput.getId();
        String source = routeInput.getSource() != null ? routeInput.getSource().trim() : "";
        String routeName = routeInput.getRoute() != null ? routeInput.getRoute().trim() : "";
        String destination = routeInput.getDestination() != null ? routeInput.getDestination().trim() : "";

        routeInput.setSource(source);
        routeInput.setRoute(routeName);
        routeInput.setDestination(destination);
        String newId = source + "-" + routeName + "-" + destination;
        routeInput.setId(newId);

        RouteInput saved = this.firestoreTemplate.save(routeInput).block();
        if (oldId != null && !oldId.isBlank() && !oldId.equals(newId)) {
            this.firestoreTemplate.deleteById(Mono.just(oldId), Route.class).block();
        }

        Route route = new Route();
        if (saved != null) {
            route.setId(saved.getId());
            route.setSource(saved.getSource());
            route.setRoute(saved.getRoute());
            route.setDestination(saved.getDestination());
            if (saved.getPoints() != null) {
                List<Point> points = new ArrayList<>();
                for (top.productivitytools.waypoints.api.models.PointInput pi : saved.getPoints()) {
                    points.add(new Point(pi.getName(), pi.getOdometer(), pi.getDistance(), pi.getComment(), pi.getTypes()));
                }
                route.setPoints(points);
            }
        }
        return route;
    }

    @MutationMapping
    public Route RemoveOdometers(@Argument("id") String id) {
        Route route = this.firestoreTemplate.findById(Mono.just(id), Route.class).block();
        if (route != null && route.getPoints() != null) {
            List<Point> updatedPoints = new ArrayList<>();
            for (Point point : route.getPoints()) {
                    point.setOdometer(null);
                    updatedPoints.add(point);
                }
            
            route.setPoints(updatedPoints);
            this.firestoreTemplate.save(route).block();
        }
        return route;
    }

    @MutationMapping
    public String DeleteRoute(@Argument("id") String id) {
        this.firestoreTemplate.deleteById(Mono.just(id), Route.class).block();
        return "deleted";
    }

    @MutationMapping
    public Route DuplicateRoute(@Argument("id") String id) {
        Route route = this.firestoreTemplate.findById(Mono.just(id), Route.class).block();
        if (route != null) {
            String source = route.getSource() != null ? route.getSource().trim() : "";
            String routeName = (route.getRoute() != null ? route.getRoute().trim() : "") + "-copy";
            String destination = route.getDestination() != null ? route.getDestination().trim() : "";
            route.setRoute(routeName);
            route.setId(source + "-" + routeName + "-" + destination);
            return this.firestoreTemplate.save(route).block();
        }
        return null;
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
