package com.stompleague.authentication.unit.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.stompleague.authentication.model.dto.RegistrationRequest;
import com.stompleague.authentication.model.dto.VerificationRequest;
import com.stompleague.authentication.model.entity.User;
import com.stompleague.authentication.repository.UserRepository;
import com.stompleague.authentication.service.UserCredentialService;
import com.stompleague.authentication.service.UserService;
import com.stompleague.authentication.service.UserVerificationCodeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
public class UserServiceTest {

  public static final String EMAIL = "alex.redfearn@stompleague.com";
  public static final String PASSWORD = "Pa55w0rd!";
  public static final String VERIFICATION_CODE = "000000";

  private final UserService userService;

  @Autowired
  public UserServiceTest(UserService userService) {
    this.userService = userService;
  }

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private UserCredentialService userCredentialService;

  @MockBean
  private UserVerificationCodeService userVerificationCodeService;

  @BeforeEach
  public void resetMocks() {
    reset(userRepository, userCredentialService, userVerificationCodeService);
  }

  @Test
  public void givenNewUser_whenCreate_thenSave() {
    // GIVEN
    RegistrationRequest registrationRequest = new RegistrationRequest()
      .setEmail(EMAIL)
      .setPassword(PASSWORD);

    User user = new User()
      .setEmail(registrationRequest.getEmail())
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));

    // WHEN
    when(userRepository.existsByEmail(registrationRequest.getEmail()))
      .thenReturn(false);

    when(userRepository.save(any(User.class)))
      .thenReturn(user);

    doNothing().when(userCredentialService).save(user, registrationRequest.getPassword());
    doNothing().when(userVerificationCodeService).generate(user.getEmail());

    userService.create(registrationRequest);

    // THEN The user should be saved
    verify(userRepository, times(1)).save(any(User.class));
    // AND Their credential should be saved
    verify(userCredentialService, times(1)).save(user, registrationRequest.getPassword());
    // AND An OTP should be generated and sent to the email
    verify(userVerificationCodeService, times(1)).generate(user.getEmail());
  }

  @Test
  public void givenExistingUser_whenCreate_ThenDoNotThrowErrorAndDoNotSave() {
    // GIVEN
    RegistrationRequest registrationRequest = new RegistrationRequest()
      .setEmail(EMAIL)
      .setPassword(PASSWORD);

    // WHEN
    when(userRepository.existsByEmail(registrationRequest.getEmail()))
      .thenReturn(true);

    userService.create(registrationRequest);

    // THEN
    verify(userRepository, never()).save(any(User.class));
    verify(userCredentialService, never()).save(any(User.class), anyString());
    verify(userVerificationCodeService, never()).generate(anyString());
  }

  @Test
  public void givenExistingUnverifiedUser_whenRecognisedEmailAndVerificationCodePassed_thenUpdateToVerified() {
    // GIVEN
    User unverifiedUser = new User()
      .setId(1L)
      .setEmail(EMAIL)
      .setVerified(false)
      .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));

    // WHEN
    VerificationRequest verificationRequest = new VerificationRequest()
      .setEmail(unverifiedUser.getEmail())
      .setCode(VERIFICATION_CODE);

    when(userVerificationCodeService.verify(EMAIL, VERIFICATION_CODE))
      .thenReturn(true);

    when(userRepository.findByEmail(EMAIL))
      .thenReturn(unverifiedUser);

    // Mock saving the now verified user
    User verifiedUser = new User()
      .setId(unverifiedUser.getId())
      .setEmail(unverifiedUser.getEmail())
      .setVerified(true)
      .setCreatedDate(unverifiedUser.getCreatedDate());

    when(userRepository.save(verifiedUser))
      .thenReturn(verifiedUser);

    userService.verify(verificationRequest);

    // THEN The user should be updated to verified true in the database
    verify(userRepository, times(1)).save(verifiedUser);
  }

}
