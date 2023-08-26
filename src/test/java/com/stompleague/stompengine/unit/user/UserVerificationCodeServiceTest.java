package com.stompleague.stompengine.unit.user;

import static com.stompleague.stompengine.unit.user.UserServiceTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.stompleague.stompengine.repository.UserVerificationCodeRepository;
import com.stompleague.stompengine.service.UserVerificationCodeService;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserVerificationCodeServiceTest {

  private final UserVerificationCodeService userVerificationCodeService;

  @Autowired
  public UserVerificationCodeServiceTest(UserVerificationCodeService userVerificationCodeService) {
    this.userVerificationCodeService = userVerificationCodeService;
  }

  @MockBean
  private UserVerificationCodeRepository userVerificationCodeRepository;

  @BeforeEach()
  public void resetMock() {
    reset(userVerificationCodeRepository);
  }

  @Test
  public void givenEmail_whenGenerateVerificationCode_thenSetEmailKeyAndSixDigitValue() {
    // WHEN
    doNothing().when(userVerificationCodeRepository).set(anyString(), anyString());

    userVerificationCodeService.generate(EMAIL);

    // THEN
    ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

    verify(userVerificationCodeRepository, times(1)).set(anyString(), stringArgumentCaptor.capture());

    String verificationCode = stringArgumentCaptor.getValue();
    assertNotNull(verificationCode);
    assertEquals(6, verificationCode.length());
  }

  @Test
  public void givenEmailAndVerificationCode_whenEmailAndVerificationCodeExists_thenReturnTrue() {
    // WHEN
    when(userVerificationCodeRepository.exists(EMAIL, VERIFICATION_CODE))
      .thenReturn(true);

    boolean verify = userVerificationCodeService.verify(EMAIL, VERIFICATION_CODE);

    // THEN
    verify(userVerificationCodeRepository, times(1)).exists(EMAIL, VERIFICATION_CODE);
    assertTrue(verify);
  }

}
