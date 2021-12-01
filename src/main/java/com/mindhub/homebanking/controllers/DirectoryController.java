package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.DirectoryDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Directory;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class DirectoryController {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/admin/directories")
    public List<DirectoryDTO> getListContact() {
        return directoryRepository.findAll().stream().map(contact -> new DirectoryDTO(contact)).collect(toList());
    }

    @GetMapping("/admin/directories/{id}")
    public DirectoryDTO getContact(@PathVariable Long id){
        return directoryRepository.findById(id).map(contact -> new DirectoryDTO(contact)).orElse(null);
    }

    @PostMapping("/clients/current/directories")
    public ResponseEntity<Object> createContact(
            @RequestParam String holder,
            @RequestParam String account,
            @RequestParam String email,
            @RequestParam String observation,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        if(holder.isEmpty() || account.isEmpty() || email.isEmpty()){
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        Set<Directory> accountContact = signClient.getDirectories().stream().filter(contact -> contact.getAccountNumber().equals(account)).collect(Collectors.toSet());
        if(accountContact.size() != 0){
            return new ResponseEntity<>("Contact created previously.", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account1 -> account1.getNumber().equals(account)).collect(Collectors.toSet());
        if(clientAccount.size() != 0){
            return new ResponseEntity<>("Account associated to your repository.", HttpStatus.FORBIDDEN);
        }

        directoryRepository.save(new Directory(signClient,holder,account,email,observation));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/directories/{id}")
    public DirectoryDTO getContactClient(@PathVariable Long id){
        return directoryRepository.findById(id).map(contact -> new DirectoryDTO(contact)).orElse(null);
    }

    @PutMapping("/clients/current/directories/{id}")
    public ResponseEntity<Object> changeContact(
            @PathVariable Long id,
            @RequestParam String holder,
            @RequestParam String account,
            @RequestParam String email,
            @RequestParam String observation,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        Account accountRegister = accountRepository.findByNumber(account);
        Directory contact = directoryRepository.findById(id).orElse(null);

        contact.setAccountHolder(holder);
        contact.setAccountNumber(account);
        contact.setEmail(email);
        contact.setObservation(observation);

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account1 -> account1.getNumber().equals(accountRegister.getNumber())).collect(Collectors.toSet());
        if(clientAccount.size() != 0){
            return new ResponseEntity<>("Account associated to your repository.", HttpStatus.FORBIDDEN);
        }

        directoryRepository.save(contact);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/directories/{id}")
    public ResponseEntity<Object> deleteContact(
            @PathVariable Long id) {

        Directory contact = directoryRepository.findById(id).orElse(null);

        directoryRepository.deleteById(contact.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
