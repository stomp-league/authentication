package com.stompleague.stompengine.service;

import com.stompleague.stompengine.model.dto.RegistrationRequest;
import com.stompleague.stompengine.model.dto.VerificationRequest;
import com.stompleague.stompengine.model.entity.User;
import com.stompleague.stompengine.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserCredentialService userCredentialService;
  private final OneTimePasswordService oneTimePasswordService;

  public void create(RegistrationRequest registrationRequest) {
    log.debug("create(RegistrationRequest), {}", registrationRequest);

    if (this.userRepository.existsByEmail(registrationRequest.getEmail())) {
      return;
    }

    User user = this.userRepository.save(new User()
      .setEmail(registrationRequest.getEmail())
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC))
    );

    userCredentialService.save(user, registrationRequest.getPassword());
    oneTimePasswordService.generate(user.getEmail());
  }

  public void verify(VerificationRequest verificationRequest) {
    log.debug("verify(VerificationRequest), {}", verificationRequest);
    User user = this.userRepository.findByEmail(verificationRequest.getEmail());

    if (user != null && this.oneTimePasswordService.verify(verificationRequest.getEmail(), verificationRequest.getCode())) {
      this.userRepository.save(user.setVerified(true));
    } else {
      throw new RuntimeException();
    }
  }

}
