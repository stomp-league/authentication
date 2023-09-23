package com.stompleague.authentication.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "USER_CREDENTIAL")
public class UserCredential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  @Column(name = "HASH")
  private String hash;

  @Column(name = "SALT")
  private String salt;

  @Column(name = "CREATED_DATE")
  private LocalDateTime createdDate;

}
