package com.techelevator.service;

import com.techelevator.model.DadJokeDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;

public class DadJokeService {

    private static final String DAD_JOKE_URL = "https://icanhazdadjoke.com/";
    private final RestTemplate restTemplate = new RestTemplate();


    public String getDadJoke() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity httpRequestEntity = new HttpEntity(headers);

        String joke = null;
        try{
            ResponseEntity<DadJokeDto> httpResponseEntity = restTemplate.exchange(DAD_JOKE_URL, HttpMethod.GET, httpRequestEntity, DadJokeDto.class);
            DadJokeDto httpResponseBody = httpResponseEntity.getBody();
            if (httpResponseBody != null) {
                joke = httpResponseBody.getJoke();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {}

        return joke;
    }

}
