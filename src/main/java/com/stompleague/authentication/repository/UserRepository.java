package com.stompleague.authentication.repository;

import com.stompleague.authentication.model.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<User, BigInteger> {

  boolean existsByEmail(String email);

  User findByEmail(String email);

}
