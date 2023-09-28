package com.stompleague.authentication.integration.identity;

import com.stompleague.authentication.integration.BaseIT;
import com.stompleague.authentication.model.dto.RegistrationRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class IdentityIT extends BaseIT {

  public static final String IDENTITY_ENDPOINT = "/identity";

  @Test
  public void givenNewIdentity_whenCreateRequest_thenReturnCreated() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setEmail("roger.badger@stompleague.com")
      .setPassword("Pa55w0rd!");

    // WHEN
    webClient.post()
      .uri(IDENTITY_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isCreated();
  }

  @Test
  public void givenNewIdentity_whenCreateRequestWithMissingPassword_thenReturnBadRequest() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setEmail("roger.badger@stompleague.com");

    // WHEN
    webClient.post()
      .uri(IDENTITY_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isBadRequest();
  }

  @Test
  public void givenNewIdentity_whenCreateRequestWithMissingEmail_thenReturnBadRequest() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setPassword("Pa55w0rd!");

    // WHEN
    webClient.post()
      .uri(IDENTITY_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isBadRequest();
  }

}
