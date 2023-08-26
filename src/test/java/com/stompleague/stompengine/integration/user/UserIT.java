package com.stompleague.stompengine.integration.user;

import com.stompleague.stompengine.integration.BaseIT;
import com.stompleague.stompengine.model.dto.RegistrationRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserIT extends BaseIT {

  public static final String USER_ENDPOINT = "/user";

  @Test
  public void givenNewUser_whenCreateRequest_thenReturnCreated() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setEmail("roger.badger@stompleague.com")
      .setPassword("Pa55w0rd!");

    // WHEN
    webClient.post()
      .uri(USER_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isCreated();
  }

  @Test
  public void givenNewUser_whenCreateRequestWithMissingPassword_thenReturnBadRequest() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setEmail("roger.badger@stompleague.com");

    // WHEN
    webClient.post()
      .uri(USER_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isBadRequest();
  }

  @Test
  public void givenNewUser_whenCreateRequestWithMissingEmail_thenReturnBadRequest() {
    // GIVEN
    RegistrationRequest request = new RegistrationRequest()
      .setPassword("Pa55w0rd!");

    // WHEN
    webClient.post()
      .uri(USER_ENDPOINT)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(request)
      .exchange()

      // THEN
      .expectStatus().isBadRequest();
  }

}
