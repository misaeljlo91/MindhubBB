package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountCardRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @GetMapping("/admin/cards")
    public List<CardDTO> getCards() {
        return cardRepository.findAll().stream().map(card -> new CardDTO(card)).collect(toList());
    }

    @GetMapping("/admin/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id).map(card -> new CardDTO(card)).orElse(null);
    }

    @PostMapping("/clients/current/debit-cards")
    public ResponseEntity<Object> createDebitCard(
            @RequestParam String account,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        Account accountClient = accountRepository.findByNumber(account);

        if(account.isEmpty()){
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        Set<Card> debitCardClient = signClient.getCards().stream().filter(card -> card.getType().equals(CardType.Debit)).collect(Collectors.toSet());
        if(debitCardClient.size() >= 3){
            return new ResponseEntity<>("Maximum debit cards allowed. Please, if some card  have not any account associated, first delete it and come back to request a new debit card.", HttpStatus.FORBIDDEN);
        }

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number2 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number3 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number4 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int) (Math.random() * (maxCVV - minCVV + 1)) + minCVV;

        Card debitCard = new Card(signClient, CardType.Debit, number1+" "+number2+" "+number3+" "+number4, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5));

        cardRepository.save(debitCard);
        accountCardRepository.save(new AccountCard(accountClient, debitCard, accountClient.getNumber(), debitCard.getNumber(), debitCard.getCvv(), accountClient.getBalance()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/debit-cards/{id}")
    public CardDTO getCardClient(
            @PathVariable Long id,
            Authentication authentication){

        Client signClient = clientRepository.findByEmail(authentication.getName());

        return cardRepository.findById(id).map(card -> new CardDTO(card)).orElse(null);
    }

    @PutMapping("/clients/current/debit-cards/{id}")
    public ResponseEntity<Object> changeData(
            @PathVariable Long id,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findById(id).orElse(null);

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number2 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number3 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number4 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int) (Math.random() * (maxCVV - minCVV + 1)) + minCVV;

        card.setNumber(number1+" "+number2+" "+number3+" "+number4);
        card.setCvv(cvv);
        card.setFromDate(LocalDateTime.now());
        card.setThruDate(LocalDateTime.now().plusYears(5));

        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clients/current/debit-cards/{id}")
    public ResponseEntity<Object> deleteCard(
            @PathVariable Long id) {

        Card card = cardRepository.findById(id).orElse(null);

        cardRepository.deleteById(card.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}