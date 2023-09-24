package com.stompleague.authentication.service;

import com.stompleague.authentication.repository.IdentityVerificationCodeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
public class IdentityVerificationCodeService {

  private final IdentityVerificationCodeRepository identityVerificationCodeRepository;

  @Autowired
  public IdentityVerificationCodeService(IdentityVerificationCodeRepository identityVerificationCodeRepository) {
    this.identityVerificationCodeRepository = identityVerificationCodeRepository;
  }

  public void generate(String email) {
    log.debug("generate(String), {}", email);

    SecureRandom secureRandom = new SecureRandom();
    int randomNumber = secureRandom.nextInt(900000) + 100000;

    String code = String.valueOf(randomNumber);

    this.identityVerificationCodeRepository.set(email, code);
  }

  public boolean verify(String email, String code) {
    return this.identityVerificationCodeRepository.exists(email, code);
  }

}
