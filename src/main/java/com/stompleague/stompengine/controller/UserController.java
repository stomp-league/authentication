package com.stompleague.stompengine.controller;

import com.stompleague.stompengine.model.dto.RegistrationRequest;
import com.stompleague.stompengine.model.dto.VerificationRequest;
import com.stompleague.stompengine.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {

  private final UserService userService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void create(@RequestBody RegistrationRequest registrationRequest) {
    log.debug("create(RegistrationRequest), {}", registrationRequest);
    this.userService.create(registrationRequest);
  }

  @PostMapping("verification")
  public void verify(@RequestBody VerificationRequest verificationRequest) {
    log.debug("verify(VerificationRequest), {}", verificationRequest);
    this.userService.verify(verificationRequest);
  }

}