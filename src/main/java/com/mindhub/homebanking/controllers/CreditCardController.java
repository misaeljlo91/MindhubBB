package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreditCardDTO;
import com.mindhub.homebanking.dtos.PayCreditCardAccountDTO;
import com.mindhub.homebanking.dtos.PayCreditCardCardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CreditCardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCardRepository transactionCardRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @GetMapping("/admin/credit-cards")
    public List<CreditCardDTO> getCreditCards() {
        return creditCardRepository.findAll().stream().map(card -> new CreditCardDTO(card)).collect(toList());
    }

    @GetMapping("/admin/credit-cards/{id}")
    public CreditCardDTO getCreditCard(@PathVariable Long id){
        return creditCardRepository.findById(id).map(card -> new CreditCardDTO(card)).orElse(null);
    }

    @Transactional
    @PostMapping("/clients/current/pay-card-with-account")
    public ResponseEntity<Object> payCardAccount(
            @RequestBody PayCreditCardAccountDTO payCardAccount,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Account originAccount = accountRepository.findByNumber(payCardAccount.getNumber());
        AccountCard originCard = accountCardRepository.findByAccount(originAccount);

        CreditCard creditCard = creditCardRepository.findById(payCardAccount.getId()).orElse(null);

        if(payCardAccount.getNumber().isEmpty() || String.valueOf(payCardAccount.getAmount()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getNumber() == null){
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account -> account.getNumber().contains(originAccount.getNumber())).collect(Collectors.toSet());
        if(clientAccount.size() == 0){
            return new ResponseEntity<>("Account not associated with the client.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getBalance() < payCardAccount.getAmount()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payCardAccount.getAmount() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payCardAccount.getAmount() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(payCardAccount.getAmount() > creditCard.getBalance()){
            return new ResponseEntity<>("The amount cannot be greater than balance.", HttpStatus.FORBIDDEN);
        }

        if(creditCard.getBalance() <= 0){
            return new ResponseEntity<>("Paid credit card.", HttpStatus.FORBIDDEN);
        }

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        originAccount.setBalance(originAccount.getBalance() - payCardAccount.getAmount());
        originCard.setBalance(originCard.getBalance() - payCardAccount.getAmount());

        creditCard.setAmountLimit(creditCard.getAmountLimit() + payCardAccount.getAmount());
        creditCard.setBalance(creditCard.getBalance() - payCardAccount.getAmount());

        transactionRepository.save(new Transaction(originAccount, originAccount.getNumber(), TransactionType.Payment, payCardAccount.getAmount(), "Pay credit card", "P"+numberDescription, creditCard.getCardholder(), creditCard.getNumber(), originAccount.getBalance(), LocalDateTime.now()));
        transactionCardRepository.save(new TransactionCard(creditCard, TransactionType.Credit, "Pay from "+originAccount.getNumber(), "C"+numberDescription, creditCard.getAmountLimit(), creditCard.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/pay-card-with-debit-card")
    public ResponseEntity<Object> payCreditCardDebit(
            @RequestBody PayCreditCardCardDTO payCreditCardDebit,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Card card = cardRepository.findByNumber(payCreditCardDebit.getNumber());

        if(payCreditCardDebit.getNumber().isEmpty() || payCreditCardDebit.getDate().isEmpty() || String.valueOf(payCreditCardDebit.getCvv()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(card == null){
            return new ResponseEntity<>("This card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(payCreditCardDebit.getCvv() != card.getCvv()){
            return new ResponseEntity<>("Invalid CVV.", HttpStatus.FORBIDDEN);
        }

        AccountCard originCard = accountCardRepository.findByCard(card);
        Account originAccount = accountRepository.findByNumber(originCard.getAccount().getNumber());

        CreditCard creditCard = creditCardRepository.findById(payCreditCardDebit.getId()).orElse(null);

        if(originCard.getBalance() < payCreditCardDebit.getAmount()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payCreditCardDebit.getAmount() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payCreditCardDebit.getAmount() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(payCreditCardDebit.getAmount() > creditCard.getBalance()){
            return new ResponseEntity<>("The amount cannot be greater than balance.", HttpStatus.FORBIDDEN);
        }

        if(creditCard.getBalance() <= 0){
            return new ResponseEntity<>("Paid credit card.", HttpStatus.FORBIDDEN);
        }

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        originAccount.setBalance(originAccount.getBalance() - payCreditCardDebit.getAmount());
        originCard.setBalance(originCard.getBalance() - payCreditCardDebit.getAmount());

        creditCard.setAmountLimit(creditCard.getAmountLimit() + payCreditCardDebit.getAmount());
        creditCard.setBalance(creditCard.getBalance() - payCreditCardDebit.getAmount());

        transactionRepository.save(new Transaction(originAccount, originAccount.getNumber(), TransactionType.Payment, payCreditCardDebit.getAmount(), "Pay credit card", "P"+numberDescription, creditCard.getCardholder(), creditCard.getNumber(), originAccount.getBalance(), LocalDateTime.now()));
        transactionCardRepository.save(new TransactionCard(creditCard, TransactionType.Credit, "Pay from "+originAccount.getNumber(), "C"+numberDescription, creditCard.getAmountLimit(), creditCard.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/pay-card-with-credit-card")
    public ResponseEntity<Object> payCreditCardCredit(
            @RequestBody PayCreditCardCardDTO payCardCredit,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        CreditCard payCard = creditCardRepository.findById(payCardCredit.getId()).orElse(null);
        CreditCard creditCard = creditCardRepository.findByNumber(payCardCredit.getNumber());

        if(payCardCredit.getNumber().isEmpty() || payCardCredit.getDate().isEmpty() || String.valueOf(payCardCredit.getCvv()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(creditCard == null){
            return new ResponseEntity<>("This card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(creditCard.getNumber() == payCard.getNumber()){
            return new ResponseEntity<>("You cannot use the same card that you are going to pay.", HttpStatus.FORBIDDEN);
        }

        if(payCardCredit.getCvv() != creditCard.getCvv()){
            return new ResponseEntity<>("Invalid CVV.", HttpStatus.FORBIDDEN);
        }

        if(creditCard.getAmountLimit() < payCardCredit.getAmount()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payCardCredit.getAmount() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payCardCredit.getAmount() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(payCard.getBalance() <= 0){
            return new ResponseEntity<>("Paid loan.", HttpStatus.FORBIDDEN);
        }

        creditCard.setAmountLimit(creditCard.getAmountLimit() - payCardCredit.getAmount());
        creditCard.setBalance(creditCard.getBalance() + payCardCredit.getAmount());

        payCard.setAmountLimit(payCard.getAmountLimit() + payCardCredit.getAmount());
        payCard.setBalance(payCard.getBalance() - payCardCredit.getAmount());

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        transactionCardRepository.save(new TransactionCard(payCard, TransactionType.Credit, "Pay from "+creditCard.getNumber(), "C"+numberDescription, payCard.getAmountLimit(), payCard.getBalance(), LocalDateTime.now()));
        transactionCardRepository.save(new TransactionCard(creditCard, TransactionType.Debit, "Pay to "+payCard.getNumber(), "D"+numberDescription, creditCard.getAmountLimit(), creditCard.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/clients/current/credit-cards")
    public ResponseEntity<Object> createCreditCard(
            @RequestParam CardColor color,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Set<CreditCard> creditCard = signClient.getCreditCards().stream().filter(card -> card.getType().equals(CardType.Credit)).collect(Collectors.toSet());
        if(creditCard.size() >= 3){
            return new ResponseEntity<>("Maximum credit cards allowed.", HttpStatus.FORBIDDEN);
        }

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number2 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number3 = (int)(Math.random()*(maxCard-minCard+1))+minCard;
        int number4 = (int)(Math.random()*(maxCard-minCard+1))+minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int)(Math.random()*(maxCVV-minCVV+1))+minCVV;

        if(color == CardColor.Gold){
            creditCardRepository.save(new CreditCard(signClient, CardType.Credit, color,number1+" "+number2+" "+number3+" "+number4, cvv, 15000,0, LocalDateTime.now(), LocalDateTime.now().plusYears(5)));
        }

        if(color == CardColor.Silver){
            creditCardRepository.save(new CreditCard(signClient, CardType.Credit, color,number1+" "+number2+" "+number3+" "+number4, cvv, 25000,0, LocalDateTime.now(), LocalDateTime.now().plusYears(5)));
        }

        if(color == CardColor.Titanium){
            creditCardRepository.save(new CreditCard(signClient, CardType.Credit, color,number1+" "+number2+" "+number3+" "+number4, cvv, 30000,0, LocalDateTime.now(), LocalDateTime.now().plusYears(5)));
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/credit-cards/{id}")
    public CreditCardDTO getCreditCardClient(
            @PathVariable Long id,
            Authentication authentication){

        Client signClient = clientRepository.findByEmail(authentication.getName());

        return creditCardRepository.findById(id).map(card -> new CreditCardDTO(card)).orElse(null);
    }

    @PutMapping("/clients/current/credit-cards/{id}")
    public ResponseEntity<Object> changeData(
            @PathVariable Long id,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        CreditCard creditCard = creditCardRepository.findById(id).orElse(null);

        int minCard = 1000;
        int maxCard = 9999;
        int number1 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number2 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number3 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;
        int number4 = (int) (Math.random() * (maxCard - minCard + 1)) + minCard;

        int minCVV = 100;
        int maxCVV = 999;
        int cvv = (int) (Math.random() * (maxCVV - minCVV + 1)) + minCVV;

        creditCard.setNumber(number1+" "+number2+" "+number3+" "+number4);
        creditCard.setCvv(cvv);
        creditCard.setFromDate(LocalDateTime.now());
        creditCard.setThruDate(LocalDateTime.now().plusYears(5));

        creditCardRepository.save(creditCard);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clients/current/credit-cards/{id}")
    public ResponseEntity<Object> deleteCard(
            @PathVariable Long id) {

        CreditCard creditCard = creditCardRepository.findById(id).orElse(null);

        if(creditCard.getBalance() > 0){
            return new ResponseEntity<>("To delete this card, the balance should be equal to 0.",HttpStatus.FORBIDDEN);

        }

        creditCardRepository.deleteById(creditCard.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
