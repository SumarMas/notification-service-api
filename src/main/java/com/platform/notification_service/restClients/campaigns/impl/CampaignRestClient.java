package com.platform.notification_service.restClients.campaigns.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.Ngo.NgoDto;
import com.platform.notification_service.dtos.campaign.CampaignDto;
import com.platform.notification_service.dtos.common.ErrorApi;
import com.platform.notification_service.restClients.campaigns.ICampaignRestClient;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
public class CampaignRestClient implements ICampaignRestClient {
    /**
     * RestTemplate instance for making HTTP requests.
     */
    private final RestTemplate restTemplate;
    /**
     * ObjectMapper instance for JSON processing.
     */
    private final ObjectMapper objectMapper;
    /**
     * Base URL for the user service.
     */
    private final String rootUrl;

    /**
     * Application name for internal request header.
     */
    private final String applicationName;

    /**
     * Constructs a NgoRestClient with the specified RestTemplate and root URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param rootUrlParam      the base URL for the user service,
     *                          injected from application properties
     * @param applicationNameParam the application name,
     *                             injected from application properties
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     */
    public CampaignRestClient(RestTemplate restTemplateParam,
                         @Value("${pool.campaign.url}") String rootUrlParam,
                         @Value("${spring.application.name}") String applicationNameParam,
                         ObjectMapper objectMapperParam) {
        this.rootUrl = rootUrlParam;
        this.applicationName = applicationNameParam;
        this.restTemplate = restTemplateParam;
        this.objectMapper = objectMapperParam;
    }

    /**
     * Retrieves a CampaignDto by its unique identifier.
     *
     * @param campaignId The unique identifier of the campaign.
     * @return A ResponseEntity containing the CampaignDto corresponding to the provided ID.
     */
    @Override
    public ResponseEntity<CampaignDto> getCampaignById(UUID campaignId) {
        String getUrl = rootUrl + "/api/v1/campaigns/{campaignId}";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Internal-Request", applicationName);
            HttpEntity<CampaignDto> requestEntity = new HttpEntity<>(null, headers);
            log.trace("Sending GET request to URL: {}", getUrl);
            return restTemplate.exchange(
                    getUrl,
                    HttpMethod.GET,
                    requestEntity,
                    CampaignDto.class,
                    campaignId
            );
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get data ngo: {}", ex.getMessage());
            handleError(ex);
            return null;
        }
    }
    private void handleError(HttpStatusCodeException ex) {
        try {
            ErrorApi error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorApi.class);
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CustomException(error.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CustomException(error.getMessage(), HttpStatus.NOT_FOUND);
            }
            throw new CustomException("Unexpected error from campaign-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from campaign-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
