package sbab.app.integration.journeypattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sbab.app.integration.ResponseData;

@Data
public class JourneyPatternResponse {
    @JsonProperty("ResponseData")
    private ResponseData<JourneyPattern> responseData;
}