package top.productivitytools.waypoints.api.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Document(collectionName = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteInput {
    @DocumentId
    private String id;
    private String name;
    private List<PointInput> points;
    private String direction;

}
