package com.stompleague.authentication.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "IDENTITY_CREDENTIAL")
public class IdentityCredential {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "IDENTITY_ID")
  private Identity identity;

  @Column(name = "HASH")
  private String hash;

  @Column(name = "SALT")
  private String salt;

  @Column(name = "CREATED_DATE")
  private LocalDateTime createdDate;

}
