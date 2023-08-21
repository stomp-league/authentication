package com.stompleague.stompengine.repository;

import com.stompleague.stompengine.model.entity.UserCredential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

}
