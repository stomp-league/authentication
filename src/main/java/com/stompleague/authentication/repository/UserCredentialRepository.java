package com.stompleague.authentication.repository;

import com.stompleague.authentication.model.entity.UserCredential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

}
