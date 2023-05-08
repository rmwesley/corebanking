package com.example.corebanking.repository;

import com.example.corebanking.model.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}
