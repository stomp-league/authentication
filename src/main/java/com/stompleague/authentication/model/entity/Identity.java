package com.stompleague.authentication.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "IDENTITY")
public class Identity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "VERIFIED")
  private boolean verified;

  @Column(name = "CREATED_DATE")
  private LocalDateTime createdDate;

}
