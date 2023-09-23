package com.stompleague.authentication.unit.cryptography;

import static com.stompleague.authentication.unit.user.UserServiceTest.PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stompleague.authentication.cryptography.SecureHash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecureHashTest {

  private static final String EXPECTED_HASH = "abc747a20eb37b767462760a51a9e9536fecb7c8eb13bcb4b8487c7f3a64c2c7";

  @Test
  public void givenPasswordAndSalt_whenHash_thenReturnHash() {
    // GIVEN
    String salt = "kqRggeBe";

    // WHEN
    String hash = SecureHash.hash(PASSWORD, salt);

    // THEN
    assertEquals(EXPECTED_HASH, hash);
  }

}
