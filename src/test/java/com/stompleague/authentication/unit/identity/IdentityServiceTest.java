package com.stompleague.authentication.unit.identity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.stompleague.authentication.model.dto.RegistrationRequest;
import com.stompleague.authentication.model.dto.VerificationRequest;
import com.stompleague.authentication.model.entity.Identity;
import com.stompleague.authentication.repository.IdentityRepository;
import com.stompleague.authentication.service.IdentityCredentialService;
import com.stompleague.authentication.service.IdentityService;
import com.stompleague.authentication.service.IdentityVerificationCodeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
public class IdentityServiceTest {

  public static final String EMAIL = "alex.redfearn@stompleague.com";
  public static final String PASSWORD = "Pa55w0rd!";
  public static final String VERIFICATION_CODE = "000000";

  private final IdentityService identityService;

  @Autowired
  public IdentityServiceTest(IdentityService identityService) {
    this.identityService = identityService;
  }

  @MockBean
  private IdentityRepository identityRepository;

  @MockBean
  private IdentityCredentialService identityCredentialService;

  @MockBean
  private IdentityVerificationCodeService identityVerificationCodeService;

  @BeforeEach
  public void resetMocks() {
    reset(identityRepository, identityCredentialService, identityVerificationCodeService);
  }

  @Test
  public void givenNewIdentity_whenCreate_thenSave() {
    // GIVEN
    RegistrationRequest registrationRequest = new RegistrationRequest()
      .setEmail(EMAIL)
      .setPassword(PASSWORD);

    Identity identity = new Identity()
      .setEmail(registrationRequest.getEmail())
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));

    // WHEN
    when(identityRepository.existsByEmail(registrationRequest.getEmail()))
      .thenReturn(false);

    when(identityRepository.save(any(Identity.class)))
      .thenReturn(identity);

    doNothing().when(identityCredentialService).save(identity, registrationRequest.getPassword());
    doNothing().when(identityVerificationCodeService).generate(identity.getEmail());

    identityService.create(registrationRequest);

    // THEN The identity should be saved
    verify(identityRepository, times(1)).save(any(Identity.class));
    // AND Their credential should be saved
    verify(identityCredentialService, times(1)).save(identity, registrationRequest.getPassword());
    // AND An OTP should be generated and sent to the email
    verify(identityVerificationCodeService, times(1)).generate(identity.getEmail());
  }

  @Test
  public void givenExistingIdentity_whenCreate_ThenDoNotThrowErrorAndDoNotSave() {
    // GIVEN
    RegistrationRequest registrationRequest = new RegistrationRequest()
      .setEmail(EMAIL)
      .setPassword(PASSWORD);

    // WHEN
    when(identityRepository.existsByEmail(registrationRequest.getEmail()))
      .thenReturn(true);

    identityService.create(registrationRequest);

    // THEN
    verify(identityRepository, never()).save(any(Identity.class));
    verify(identityCredentialService, never()).save(any(Identity.class), anyString());
    verify(identityVerificationCodeService, never()).generate(anyString());
  }

  @Test
  public void givenExistingUnverifiedIdentity_whenRecognisedEmailAndVerificationCodePassed_thenUpdateToVerified() {
    // GIVEN
    Identity unverifiedIdentity = new Identity()
      .setId(1L)
      .setEmail(EMAIL)
      .setVerified(false)
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));

    // WHEN
    VerificationRequest verificationRequest = new VerificationRequest()
      .setEmail(unverifiedIdentity.getEmail())
      .setCode(VERIFICATION_CODE);

    when(identityVerificationCodeService.verify(EMAIL, VERIFICATION_CODE))
      .thenReturn(true);

    when(identityRepository.findByEmail(EMAIL))
      .thenReturn(unverifiedIdentity);

    // Mock saving the now verified identity
    Identity verifiedIdentity = new Identity()
      .setId(unverifiedIdentity.getId())
      .setEmail(unverifiedIdentity.getEmail())
      .setVerified(true)
      .setCreatedDate(unverifiedIdentity.getCreatedDate());

    when(identityRepository.save(verifiedIdentity))
      .thenReturn(verifiedIdentity);

    identityService.verify(verificationRequest);

    // THEN The identity should be updated to verified true in the database
    verify(identityRepository, times(1)).save(verifiedIdentity);
  }

}
