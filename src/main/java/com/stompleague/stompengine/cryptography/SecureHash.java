package com.stompleague.stompengine.cryptography;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecureHash {

  public static String hash(String value, String salt) {
    try {
      Mac algorithm = Mac.getInstance("HmacSHA256");

      algorithm.init(new SecretKeySpec(salt.getBytes(), "HmacSHA256"));
      byte[] hash = algorithm.doFinal(value.getBytes());

      return DatatypeConverter.printHexBinary(hash).toLowerCase();
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

}
