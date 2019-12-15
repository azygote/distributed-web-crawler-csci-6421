package org.gty.crawler.slave.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class DownloadClient {

    private final RestTemplate restTemplate;

    public DownloadClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public String download(String url) {
        URI uri = UriComponentsBuilder.fromHttpUrl(url)
            .encode(StandardCharsets.UTF_8)
            .build(true)
            .toUri();

        return restTemplate.getForObject(uri, String.class);
    }
}
