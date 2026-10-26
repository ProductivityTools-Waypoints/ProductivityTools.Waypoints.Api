package top.productivitytools.waypoints.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private String name;
    private Integer odometer;
    private Integer distance;
    private String comment;
    private List<String> types;
}
