package com.stompleague.authentication.repository;

import com.stompleague.authentication.model.entity.Identity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IdentityRepository extends JpaRepository<Identity, BigInteger> {

  boolean existsByEmail(String email);

  Identity findByEmail(String email);

}
