package sbab.app.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final RestTemplate restTemplate;
    @Value("${trafiklab.key}")
    private String apiKey;
    @Value("${trafiklab.url}")
    private String url;

    public Object request(ObjectType objectType){
        var requestUrl = buildUrl(objectType.model);

        var response = restTemplate.exchange(requestUrl.getFullUrl(),
            HttpMethod.GET,
            null,
            objectType.responseClass);

        return response.getBody();
    }

    private RequestUrl buildUrl(String model){
        var requestUrl = RequestUrl.builder()
            .url(url)
            .apiKey(apiKey)
            .model(model)
            .build();

        return requestUrl;
    }

}
