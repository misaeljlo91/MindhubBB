package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/admin/accounts")
    public List<AccountDTO> getListAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @GetMapping("/admin/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @PostMapping("/admin/clients/{id}/accounts")
    public ResponseEntity<Object> createAccountADMIN(
            @RequestParam AccountType type,
            @PathVariable Long id) {

        Client client = clientRepository.findById(id).orElse(null);

        Set<Card> debitCard = client.getCards().stream().filter(card -> card.getType().equals(CardType.Debit)).collect(Collectors.toSet());
        if(debitCard.size() >= 3){
            return new ResponseEntity<>("Maximum debit cards allowed. Please, if some card  have not any account associated, first delete it and come back to request a new debit card.", HttpStatus.FORBIDDEN);
        }

        int minAccount = 100000;
        int maxAccount = 999999;
        int account = (int)(Math.random()*(maxAccount-minAccount+1))+minAccount;

        int minID = 10;
        int maxID = 99;
        int accountID = (int)(Math.random()*(maxID-minID+1))+minID;

        Account createAccount = new Account(client,"VIN-"+account+"/"+accountID,type,LocalDateTime.now(),0);

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number2 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number3 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number4 = (int)(Math.random()*(maxCard-minCard+1))+minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int)(Math.random()*(maxCVV-minCVV+1))+minCVV;

        Card createCard = new Card(client, CardType.Debit, number1+" "+number2+" "+number3+" "+number4, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));

        accountRepository.save(createAccount);
        cardRepository.save(createCard);
        accountCardRepository.save(new AccountCard(createAccount, createCard, createAccount.getNumber(), createCard.getNumber(), createCard.getCvv(), 0));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(
            @RequestParam AccountType type,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        if(signClient.getAccounts().size() >= 3){
            return new ResponseEntity<>("Maximum accounts allowed.", HttpStatus.FORBIDDEN);
        }

        Set<Card> debitCard = signClient.getCards().stream().filter(card -> card.getType().equals(CardType.Debit)).collect(Collectors.toSet());
        if(debitCard.size() >= 3){
            return new ResponseEntity<>("Maximum debit cards allowed. Please, if some card  have not any account associated, first delete it and come back to request a new debit card.", HttpStatus.FORBIDDEN);
        }

        int minAccount = 100000;
        int maxAccount = 999999;
        int account = (int)(Math.random()*(maxAccount-minAccount+1))+minAccount;

        int minID = 10;
        int maxID = 99;
        int accountID = (int)(Math.random()*(maxID-minID+1))+minID;

        Account createAccount = new Account(signClient,"VIN-"+account+"/"+accountID,type,LocalDateTime.now(),0);

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number2 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number3 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number4 = (int)(Math.random()*(maxCard-minCard+1))+minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int)(Math.random()*(maxCVV-minCVV+1))+minCVV;

        Card createCard = new Card(signClient, CardType.Debit,number1+" "+number2+" "+number3+" "+number4, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));

        accountRepository.save(createAccount);
        cardRepository.save(createCard);
        accountCardRepository.save(new AccountCard(createAccount, createCard, createAccount.getNumber(), createCard.getNumber(), createCard.getCvv(), 0));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts/{id}")
    public AccountDTO getAccountClient(
            @PathVariable Long id,
            Authentication authentication){

        Client signClient = clientRepository.findByEmail(authentication.getName());

        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @DeleteMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(
            @PathVariable Long id) {

        Account account = accountRepository.findById(id).orElse(null);

        if(account.getBalance() > 0){
            return new ResponseEntity<>("To delete this account, your balance should be equal to 0.",HttpStatus.FORBIDDEN);
        }

        accountRepository.deleteById(account.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
