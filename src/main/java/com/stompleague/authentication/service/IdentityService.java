package com.stompleague.authentication.service;

import com.stompleague.authentication.exception.BadRequestException;
import com.stompleague.authentication.exception.UnauthorizedException;
import com.stompleague.authentication.model.dto.RegistrationRequest;
import com.stompleague.authentication.model.dto.VerificationRequest;
import com.stompleague.authentication.model.entity.Identity;
import com.stompleague.authentication.repository.IdentityRepository;

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
public class IdentityService {

  private final IdentityRepository identityRepository;
  private final IdentityCredentialService identityCredentialService;
  private final IdentityVerificationCodeService identityVerificationCodeService;

  @Autowired
  public IdentityService(IdentityRepository identityRepository, IdentityCredentialService identityCredentialService, IdentityVerificationCodeService identityVerificationCodeService) {
    this.identityRepository = identityRepository;
    this.identityCredentialService = identityCredentialService;
    this.identityVerificationCodeService = identityVerificationCodeService;
  }

  public void create(@Valid RegistrationRequest registrationRequest) {
    log.debug("create(RegistrationRequest), {}", registrationRequest);

    if (this.identityRepository.existsByEmail(registrationRequest.getEmail())) {
      log.debug("Account already exists");
      return; // Send email to say you already have an account.
    }

    Identity identity = this.identityRepository.save(new Identity()
      .setEmail(registrationRequest.getEmail())
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC))
    );

    try {
      identityCredentialService.save(identity, registrationRequest.getPassword());
    } catch (IllegalArgumentException e) {
      log.debug(e.getMessage());
      throw new BadRequestException("Password is mandatory");
    }

    identityVerificationCodeService.generate(identity.getEmail());
  }

  public void verify(@Valid VerificationRequest verificationRequest) {
    log.debug("verify(VerificationRequest), {}", verificationRequest);

    if (!this.identityVerificationCodeService.verify(verificationRequest.getEmail(), verificationRequest.getCode())) {
      log.debug("Unrecognised email ({}) and code ({}) combination", verificationRequest.getEmail(), verificationRequest.getCode());
      throw new UnauthorizedException();
    }

    Identity identity = this.identityRepository.findByEmail(verificationRequest.getEmail());

    if (Optional.ofNullable(identity).isEmpty()) {
      log.debug("Strange! The identity doesn't actually exist...");
      throw new UnauthorizedException();
    }

    if (identity.isVerified()) {
      log.debug("Identity is already verified! {}", identity);
      throw new UnauthorizedException();
    }

    // Made it to the end!
    identityRepository.save(identity.setVerified(true));

  }

}
