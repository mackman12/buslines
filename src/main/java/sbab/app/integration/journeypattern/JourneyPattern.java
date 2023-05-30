package sbab.app.integration.journeypattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

@Data
public class JourneyPattern {

    @JsonProperty("LineNumber")
    private String lineNumber;

    @JsonProperty("JourneyPatternPointNumber")
    private String journeyPatternPointNumber;

}
