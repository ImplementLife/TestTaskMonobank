package com.testTask.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/deleteAll.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class RequestPayControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RequestPayController controller;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private final String create = "/requestPay/create";
    private final String getStatus = "/requestPay/getStatus";
    private final String getRandomStatus = "/requestPay/getRandomStatus";

    @Test
    public void testContext() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testGetRandomStatus() throws Exception {
        ResponseEntity<String> response;
        response = restTemplate.getForEntity("http://localhost:" + port + getRandomStatus, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("status");
    }

    @Test
    public void testCreate() throws Exception {
        MultiValueMap<String, String> vars = new LinkedMultiValueMap<>();
        vars.add("number", "1");
        vars.add("date", "2021-02-20");

        ResponseEntity<String> response;
        response = restTemplate.postForEntity("http://localhost:" + port + create, vars, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("idRequest");
    }

    @Test
    public void testCreateWithInvalidParameters() throws Exception {
        MultiValueMap<String, String> vars = new LinkedMultiValueMap<>();
        vars.add("number", "");
        vars.add("date", "2021-02-20");

        ResponseEntity<String> response;
        response = restTemplate.postForEntity("http://localhost:" + port + create, vars, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Test
    @Sql(value = {"/deleteAll.sql", "/addOneElement.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteAll.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetStatus() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + getStatus)
                .queryParam("id", 1);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("status");
    }

    @Test
    public void testGetStatusWithInvalidParameters() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + getStatus)
                .queryParam("id", -1);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}