package com.stompleague.authentication.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class IdentityVerificationCodeRepository {

  private final StringRedisTemplate stringRedisTemplate;
  private final ValueOperations<String, String> valueOperations;

  @Autowired
  public IdentityVerificationCodeRepository(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.valueOperations = this.stringRedisTemplate.opsForValue();
  }

  public void set(String key, String value) {
    valueOperations.set(key, value, 2, TimeUnit.HOURS);
  }

  public boolean exists(String key, String value) {
    if (value.equals(valueOperations.get(key))) {
      stringRedisTemplate.delete(key);
      return true;
    } else {
      return false;
    }
  }

}
