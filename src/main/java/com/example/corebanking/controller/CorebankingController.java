package com.example.corebanking.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
import com.example.corebanking.model.Client;
import com.example.corebanking.repository.ClientRepository;

@RestController
@RequestMapping("/clients")
public class CorebankingController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    private ResponseEntity<List<Client>> getClients(){
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{clientId}")
    private ResponseEntity<Client> getClient(@PathVariable Long clientId){
        Optional<Client> optAccount = clientRepository.findById(clientId);
        if(!optAccount.isPresent()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found.\n");
        }
        return new ResponseEntity<>(optAccount.get(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Long> createClient(@RequestBody ClientCreationDTO clientCreationDTO) {
        Client saved = clientRepository.saveAndFlush(new Client(clientCreationDTO.getName()));
        return new ResponseEntity<>(saved.getId(), HttpStatus.CREATED);
    }
}
