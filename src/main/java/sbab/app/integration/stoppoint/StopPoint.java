package sbab.app.integration.stoppoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StopPoint {

    @JsonProperty("StopPointNumber")
    private String stopPointNumber;

    @JsonProperty("StopPointName")
    private String stopPointName;
}
