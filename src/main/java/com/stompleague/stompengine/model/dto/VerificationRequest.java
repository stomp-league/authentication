package com.stompleague.stompengine.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerificationRequest {

  @NotBlank(message = "Email is required")
  private String email;

  @Size(min = 6, max = 6, message = "Six digit verification code required")
  private String code;

}
