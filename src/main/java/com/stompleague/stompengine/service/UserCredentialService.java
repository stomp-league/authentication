package com.stompleague.stompengine.service;

import com.stompleague.stompengine.cryptography.SecureHash;
import com.stompleague.stompengine.model.entity.User;
import com.stompleague.stompengine.model.entity.UserCredential;
import com.stompleague.stompengine.repository.UserCredentialRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCredentialService {

  private final UserCredentialRepository userCredentialRepository;

  public void save(User user, String password) {
    log.debug("save(User, String), {}, {}", user, password);

    String salt = RandomStringUtils.randomAlphanumeric(8);
    String hash = SecureHash.hash(password, salt);

    this.userCredentialRepository.save(
      new UserCredential()
        .setUser(user)
        .setHash(hash)
        .setSalt(salt)
        .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC))
    );
  }

}
