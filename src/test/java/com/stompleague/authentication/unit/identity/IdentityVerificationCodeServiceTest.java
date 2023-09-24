package com.stompleague.authentication.unit.identity;

import static com.stompleague.authentication.unit.identity.IdentityServiceTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.stompleague.authentication.repository.IdentityVerificationCodeRepository;
import com.stompleague.authentication.service.IdentityVerificationCodeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class IdentityVerificationCodeServiceTest {

  private final IdentityVerificationCodeService identityVerificationCodeService;

  @Autowired
  public IdentityVerificationCodeServiceTest(IdentityVerificationCodeService identityVerificationCodeService) {
    this.identityVerificationCodeService = identityVerificationCodeService;
  }

  @MockBean
  private IdentityVerificationCodeRepository identityVerificationCodeRepository;

  @BeforeEach()
  public void resetMock() {
    reset(identityVerificationCodeRepository);
  }

  @Test
  public void givenEmail_whenGenerateVerificationCode_thenSetEmailKeyAndSixDigitValue() {
    // WHEN
    doNothing().when(identityVerificationCodeRepository).set(anyString(), anyString());

    identityVerificationCodeService.generate(EMAIL);

    // THEN
    ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    verify(identityVerificationCodeRepository, times(1)).set(anyString(), stringArgumentCaptor.capture());

    String verificationCode = stringArgumentCaptor.getValue();
    assertNotNull(verificationCode);
    assertEquals(6, verificationCode.length());
  }

  @Test
  public void givenEmailAndVerificationCode_whenEmailAndVerificationCodeExists_thenReturnTrue() {
    // WHEN
    when(identityVerificationCodeRepository.exists(EMAIL, VERIFICATION_CODE))
      .thenReturn(true);

    boolean verify = identityVerificationCodeService.verify(EMAIL, VERIFICATION_CODE);

    // THEN
    verify(identityVerificationCodeRepository, times(1)).exists(EMAIL, VERIFICATION_CODE);
    assertTrue(verify);
  }

}
