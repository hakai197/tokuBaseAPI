package com.tokubase.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;
/**
 * Provides a RestTemplate bean configured for wiki API calls.
 * MediaWiki / Fandom requires a descriptive User-Agent header to identify the client.
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate(clientHttpRequestFactory());
        // Attach the required User-Agent to every outbound request
        rt.setInterceptors(List.of(new UserAgentInterceptor()));
        return rt;
    }
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);  // 10 s
        factory.setReadTimeout(15_000);     // 15 s
        return factory;
    }
    /** Adds a proper User-Agent so MediaWiki does not reject our requests. */
    private static class UserAgentInterceptor implements ClientHttpRequestInterceptor {
        private static final String UA =
                "TokuBaseAPI/1.0 (https://github.com/your-org/tokubase; contact@tokubase.io) " +
                "Spring-RestTemplate/6";
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().set("User-Agent", UA);
            return execution.execute(request, body);
        }
    }
}
