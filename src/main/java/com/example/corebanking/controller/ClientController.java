package com.example.corebanking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.corebanking.dto.ClientCreationDTO;
import com.example.corebanking.model.Account;
import com.example.corebanking.model.Client;
import com.example.corebanking.repository.ClientRepository;
import com.example.corebanking.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients API")
public class ClientController {

  @Autowired private ClientRepository clientRepository;
  @Autowired private AccountService accountService;

  @Operation(summary = "Get list of all clients")
  @GetMapping
  private ResponseEntity<List<Client>> getClients() {
    return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
  }

  @Operation(summary = "Get client by id")
  @GetMapping("/{clientId}")
  private ResponseEntity<Client> getClient(@PathVariable Long clientId) {
    Client client = clientRepository.getReferenceById(clientId);
    return new ResponseEntity<>(client, HttpStatus.OK);
  }

  @Operation(summary = "Creates and adds a client to the database")
  @PostMapping
  public ResponseEntity<Client> createClient(@RequestBody ClientCreationDTO clientCreationDTO) {
    Client client = new Client(clientCreationDTO.getName());
    clientRepository.saveAndFlush(client);
    return new ResponseEntity<>(client, HttpStatus.CREATED);
  }

  @Operation(summary = "Creates and adds an account to a client in the database")
  @PostMapping("/{clientId}")
  public ResponseEntity<Account> createAccount(@PathVariable Long clientId) {
    Client client = clientRepository.getReferenceById(clientId);
    Account createdAccount = accountService.createNewAccountFor(client);
    return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
  }
}
