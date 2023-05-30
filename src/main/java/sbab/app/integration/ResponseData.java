package sbab.app.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ResponseData <T> {
    @JsonProperty("Result")
    private List<T> result;
}
