package sbab.app.integration.stoppoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sbab.app.integration.ResponseData;

@Data
public class StopPointResponse {
    @JsonProperty("ResponseData")
    private ResponseData<StopPoint> responseData;
}