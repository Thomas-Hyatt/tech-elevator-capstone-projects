package com.techelevator.tebucks.service;

import com.techelevator.tebucks.model.LogDto;
import com.techelevator.tebucks.model.TokenDto;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class IrsLogService {

    private static final String API_BASE_URL = "https://te-pgh-api.azurewebsites.net/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public LogDto logTransfer(LogDto newLogDto) {

        LogDto returnedLogDto = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);

        HttpEntity<LogDto> request = new HttpEntity<>(newLogDto, headers);
        try {
            returnedLogDto = restTemplate.exchange(API_BASE_URL + "api/TxLog", HttpMethod.POST, request, LogDto.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {}

        return returnedLogDto;

    }

}
