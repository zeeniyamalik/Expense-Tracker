package com.zeeniya.expense_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TestApiService {
    private final RestClient restClient;

    public TestApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getApiResponse(){
//       return restClient
//               .get()
//               .uri("https://api.github.com")
//               .retrieve()
//               .body(String.class);
        return restClient
                .post()
                .uri("https://httpbin.org/post")
                .body("""
                        {
                            "message": "hello world"
                        }
                        """)
                .retrieve()
                .body(String.class);
    }
}
