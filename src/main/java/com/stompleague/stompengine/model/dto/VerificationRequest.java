package com.stompleague.stompengine.model.dto;

import lombok.Data;

@Data
public class VerificationRequest {

  private String email;
  private String code;

}
