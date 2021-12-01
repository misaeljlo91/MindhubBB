package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/clients")
    public List<ClientDTO> getListClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(clientRepository.findByEmail(email) !=  null || clientRepository.findByUsername(username) !=  null) {
            if(clientRepository.findByEmail(email) !=  null){
                return new ResponseEntity<>("Email is in use. Please, try again.", HttpStatus.FORBIDDEN);
            }
            if(clientRepository.findByUsername(username) !=  null) {
                return new ResponseEntity<>("Username is in use. Please, try again.", HttpStatus.FORBIDDEN);
            }
        }

        if(password.length() < 8 || password.length() > 12){
            return new ResponseEntity<>("Your password must be 8-12 characters long.", HttpStatus.FORBIDDEN);
        }

        clientRepository.save(new Client(firstName, lastName, username, email, passwordEncoder.encode(password), "/web/assets/profile.png", ClientRole.USER));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/clients/password")
    public ResponseEntity<Object> changePassword(
            @RequestParam String email,
            @RequestParam String password){

        Client client = clientRepository.findByEmail(email);

        if(client == null){
            return new ResponseEntity("Client not found", HttpStatus.FORBIDDEN);
        }

        if(password.length() < 8 || password.length() > 12){
            return new ResponseEntity<>("Your password must be 8-12 characters long.", HttpStatus.FORBIDDEN);
        }

        client.setPassword(passwordEncoder.encode(password));

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clients/username")
    public ResponseEntity<Object> changeUsername(
            @RequestParam String email,
            @RequestParam String username){

        Client client = clientRepository.findByEmail(email);

        if(client == null){
            return new ResponseEntity("Client not found", HttpStatus.FORBIDDEN);
        }

        if(clientRepository.findByUsername(username) !=  null) {
            return new ResponseEntity<>("Username is in use. Please, try again.", HttpStatus.FORBIDDEN);
        }

        client.setUsername(username);

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(client1 -> new ClientDTO(client1)).orElse(null);
    }

    @PutMapping("/admin/clients/{id}")
    public ResponseEntity<Object> changeDataADMIN(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam ClientRole role,
            @PathVariable Long id) {

        Client client = clientRepository.findById(id).orElse(null);

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setUsername(username);
        client.setEmail(email);
        client.setRole(role);

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/clients/{id}")
    public ResponseEntity<Object> deleteDataADMIN(
            @PathVariable Long id) {

        Client client = clientRepository.findById(id).orElse(null);

        if(client.getAccounts().size() > 0){
            return new ResponseEntity<>("This client has one o more associated accounts. To delete it verify if the account/s do not present positive balance to continue.", HttpStatus.FORBIDDEN);
        }

        if(client.getClientLoans().size() > 0){
            return new ResponseEntity<>("This client has one o more associated loans without paying. To delete it verify if the loan/s present amount equal 0.", HttpStatus.FORBIDDEN);

        }

        clientRepository.deleteById(client.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
        Client signClient = clientRepository.findByEmail(authentication.getName());
        ClientDTO signClientDTO = new ClientDTO(signClient);
        return signClientDTO;
    }

    @GetMapping("/clients/current/{id}")
    public ClientDTO getClientAuth(@PathVariable Long id){
        return clientRepository.findById(id).map(client1 -> new ClientDTO(client1)).orElse(null);
    }

    @DeleteMapping("/clients/current/{id}")
    public ResponseEntity<Object> deleteDataClient(
            @PathVariable Long id) {

        Client client = clientRepository.findById(id).orElse(null);

        if(client.getAccounts().size() > 0){
            return new ResponseEntity<>("You have one o more associated accounts to your user. Go to the accounts page and delete it to continue.", HttpStatus.FORBIDDEN);
        }

        if(client.getClientLoans().size() > 0){
            return new ResponseEntity<>("You have one o more associated loans to your user without paying. Go to the loans page and pay it to continue.", HttpStatus.FORBIDDEN);

        }

        if(client.getCreditCards().size() > 0){
            return new ResponseEntity<>("You have one o more associated credit cards to your user. Go to the credit cards page and verify that balance be equal 0 to continue.", HttpStatus.FORBIDDEN);
        }

        clientRepository.deleteById(client.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clients/current/personal")
    public ResponseEntity<Object> changeData(
            @RequestParam String firstName,
            @RequestParam String lastName,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        client.setFirstName(firstName);
        client.setLastName(lastName);

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clients/current/password")
    public ResponseEntity<Object> changePassword(
            @RequestParam String password,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if(password.length() < 8 || password.length() > 12){
            return new ResponseEntity<>("Your password must be 8-12 characters long.", HttpStatus.FORBIDDEN);
        }

        client.setPassword(passwordEncoder.encode(password));

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clients/current/username")
    public ResponseEntity<Object> changeUsername(
            @RequestParam String username,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if(clientRepository.findByUsername(username) != null){
            return new ResponseEntity<>("Username is in use. Please, try again.", HttpStatus.FORBIDDEN);
        }

        client.setUsername(username);

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clients/current/profile")
    public ResponseEntity<Object> changeProfile(
            @RequestParam String profile,
            Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        client.setProfile(profile);

        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
