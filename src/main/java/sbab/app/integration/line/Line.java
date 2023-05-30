package sbab.app.integration.line;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Line {

    @JsonProperty("LineNumber")
    private String lineNumber;

    @JsonProperty("DefaultTransportModeCode")
    private String defaultTransportModeCode;
}
