package org.springboot;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    public void fetchExternalData() {
        // Ye ek public sample JSON API URL hai
        String url = "https://jsonplaceholder.typicode.com/posts/1"; 

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        System.out.println("API Response: " + response);
    }
}