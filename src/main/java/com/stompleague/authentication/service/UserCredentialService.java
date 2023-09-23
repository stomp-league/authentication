package com.stompleague.authentication.service;

import com.stompleague.authentication.cryptography.SecureHash;
import com.stompleague.authentication.model.entity.User;
import com.stompleague.authentication.model.entity.UserCredential;
import com.stompleague.authentication.repository.UserCredentialRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class UserCredentialService {

  private final UserCredentialRepository userCredentialRepository;

  @Autowired
  public UserCredentialService(UserCredentialRepository userCredentialRepository) {
    this.userCredentialRepository = userCredentialRepository;
  }

  public void save(User user, String password) {
    log.debug("save(User, String), {}, {}", user, password);

    if (StringUtils.isEmpty(password))
      throw new IllegalArgumentException("Password empty");

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
