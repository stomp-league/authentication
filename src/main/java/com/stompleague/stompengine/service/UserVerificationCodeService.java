package com.stompleague.stompengine.service;

import com.stompleague.stompengine.repository.UserVerificationCodeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
public class UserVerificationCodeService {

  private final UserVerificationCodeRepository userVerificationCodeRepository;

  @Autowired
  public UserVerificationCodeService(UserVerificationCodeRepository userVerificationCodeRepository) {
    this.userVerificationCodeRepository = userVerificationCodeRepository;
  }

  public void generate(String email) {
    log.debug("generate(String), {}", email);

    SecureRandom secureRandom = new SecureRandom();
    int randomNumber = secureRandom.nextInt(900000) + 100000;

    String code = String.valueOf(randomNumber);

    this.userVerificationCodeRepository.set(email, code);
  }

  public boolean verify(String email, String code) {
    return this.userVerificationCodeRepository.exists(email, code);
  }

}
