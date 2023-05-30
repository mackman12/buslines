package sbab.app.integration;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestUrl {
    private String url;
    private String model;
    private String apiKey;

    public String getFullUrl(){
        var fullUrl = this.url
            .concat("?model=")
            .concat(this.model)
            .concat("&key=")
            .concat(this.apiKey);

        return fullUrl;
    }

}
