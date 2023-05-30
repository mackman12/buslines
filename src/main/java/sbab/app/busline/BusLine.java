package sbab.app.busline;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BusLine {
    private String lineNumber;
    private List<String> busStops;
}
