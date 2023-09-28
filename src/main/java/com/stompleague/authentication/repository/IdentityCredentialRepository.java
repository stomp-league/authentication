package com.stompleague.authentication.repository;

import com.stompleague.authentication.model.entity.IdentityCredential;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityCredentialRepository extends JpaRepository<IdentityCredential, Long> {

}
