package sbab.app.integration.line;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sbab.app.integration.ResponseData;

@Data
public class LineResponse {

    @JsonProperty("ResponseData")
    private ResponseData<Line> responseData;

}