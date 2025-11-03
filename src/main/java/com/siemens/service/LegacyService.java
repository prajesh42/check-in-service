package com.siemens.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class LegacyService {
	
	Logger log= LoggerFactory.getLogger(LegacyService.class);

	@Value("${app.legacy.baseUrl}")
	private String baseUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * @param employeeId
	 * @param hours
	 * This method calls the legacy api responsible for recording employeeid and worked hour.
	 * It also handles the retries and rate limiting mechanism based on api's response time and do the fallback
	 * if it crosses the threshold limit of retries
	 */
	@CircuitBreaker(name = "legacy", fallbackMethod = "fallback")
	@Retry(name = "legacy")
	@RateLimiter(name = "legacy")
	public void recordEmployeeData(String employeeId, Double hours) {
		String url = baseUrl + "/record";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "employeeId", employeeId,
                "hours", hours
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
        	log.info("Failed to record data in legacy system. Status:", response.getStatusCode());
        }
	}
	
	public void fallback(String employeeId, Double hours, Throwable t) throws Throwable { 
		throw t;
	}
}
