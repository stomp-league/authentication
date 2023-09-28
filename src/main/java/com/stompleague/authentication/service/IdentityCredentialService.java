package com.stompleague.authentication.service;

import com.stompleague.authentication.cryptography.SecureHash;
import com.stompleague.authentication.model.entity.Identity;
import com.stompleague.authentication.model.entity.IdentityCredential;
import com.stompleague.authentication.repository.IdentityCredentialRepository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class IdentityCredentialService {

  private final IdentityCredentialRepository identityCredentialRepository;

  @Autowired
  public IdentityCredentialService(IdentityCredentialRepository identityCredentialRepository) {
    this.identityCredentialRepository = identityCredentialRepository;
  }

  public void save(Identity identity, String password) {
    log.debug("save(Identity, String), {}, {}", identity, password);

    if (StringUtils.isEmpty(password))
      throw new IllegalArgumentException("Password empty");

    String salt = RandomStringUtils.randomAlphanumeric(8);
    String hash = SecureHash.hash(password, salt);

    this.identityCredentialRepository.save(
      new IdentityCredential()
        .setIdentity(identity)
        .setHash(hash)
        .setSalt(salt)
        .setCreatedDate(LocalDateTime.now(ZoneOffset.UTC))
    );
  }

}
