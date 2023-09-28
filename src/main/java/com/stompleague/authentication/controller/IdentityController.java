package com.stompleague.authentication.controller;

import com.stompleague.authentication.model.dto.RegistrationRequest;
import com.stompleague.authentication.model.dto.VerificationRequest;
import com.stompleague.authentication.service.IdentityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("identity")
@RestController
public class IdentityController {

  private final IdentityService identityService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void create(@RequestBody RegistrationRequest registrationRequest) {
    log.debug("create(RegistrationRequest), {}", registrationRequest);
    this.identityService.create(registrationRequest);
  }

  @PostMapping("verification")
  public void verify(@RequestBody VerificationRequest verificationRequest) {
    log.debug("verify(VerificationRequest), {}", verificationRequest);
    this.identityService.verify(verificationRequest);
  }

}
