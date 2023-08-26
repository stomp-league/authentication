package com.stompleague.stompengine.service;

import com.stompleague.stompengine.exception.BadRequestException;
import com.stompleague.stompengine.exception.UnauthorizedException;
import com.stompleague.stompengine.model.dto.RegistrationRequest;
import com.stompleague.stompengine.model.dto.VerificationRequest;
import com.stompleague.stompengine.model.entity.User;
import com.stompleague.stompengine.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Validated
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserCredentialService userCredentialService;
  private final UserVerificationCodeService userVerificationCodeService;

  @Autowired
  public UserService(UserRepository userRepository, UserCredentialService userCredentialService, UserVerificationCodeService userVerificationCodeService) {
    this.userRepository = userRepository;
    this.userCredentialService = userCredentialService;
    this.userVerificationCodeService = userVerificationCodeService;
  }

  public void create(@Valid RegistrationRequest registrationRequest) {
    log.debug("create(RegistrationRequest), {}", registrationRequest);

    if (this.userRepository.existsByEmail(registrationRequest.getEmail())) {
      log.debug("Account already exists");
      return; // Send email to say you already have an account.
    }

    User user = this.userRepository.save(new User()
      .setEmail(registrationRequest.getEmail())
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC))
    );

    try {
      userCredentialService.save(user, registrationRequest.getPassword());
    } catch (IllegalArgumentException e) {
      log.debug(e.getMessage());
      throw new BadRequestException("Password is mandatory");
    }

    userVerificationCodeService.generate(user.getEmail());
  }

  public void verify(@Valid VerificationRequest verificationRequest) {
    log.debug("verify(VerificationRequest), {}", verificationRequest);

    if (!this.userVerificationCodeService.verify(verificationRequest.getEmail(), verificationRequest.getCode())) {
      log.debug("Unrecognised email ({}) and code ({}) combination", verificationRequest.getEmail(), verificationRequest.getCode());
      throw new UnauthorizedException();
    }

    User user = this.userRepository.findByEmail(verificationRequest.getEmail());

    if (Optional.ofNullable(user).isEmpty()) {
      log.debug("Strange! The user doesn't actually exist...");
      throw new UnauthorizedException();
    }

    if (user.isVerified()) {
      log.debug("User is already verified! {}", user);
      throw new UnauthorizedException();
    }

    // Made it to the end!
    userRepository.save(user.setVerified(true));

  }

}
