package com.techelevator.tebucks.service;

import com.techelevator.tebucks.model.CredentialsDto;
import com.techelevator.tebucks.model.TokenDto;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class IrsAuthenticationService {

    private static final String API_BASE_URL = "https://te-pgh-api.azurewebsites.net/";
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String JAVA_TEAM_04_USERNAME = "team04";
    private static final String JAVA_TEAM_04_PASSWORD = "password";

    public String login() {

        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setUsername(JAVA_TEAM_04_USERNAME);
        credentialsDto.setPassword(JAVA_TEAM_04_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> entity = new HttpEntity<>(credentialsDto, headers);

        String token = null;
        try {
            ResponseEntity<TokenDto> response = restTemplate.exchange(API_BASE_URL + "api/Login", HttpMethod.POST, entity, TokenDto.class);
            TokenDto body = response.getBody();
            if (body != null) {
                token = body.getToken();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {}
        return token;
    }

}
