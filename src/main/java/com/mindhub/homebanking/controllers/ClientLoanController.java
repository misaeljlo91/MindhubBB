package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.PayLoanAccountDTO;
import com.mindhub.homebanking.dtos.PayLoanCardDTO;
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
public class ClientLoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCardRepository transactionCardRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/admin/clientloans")
    public List<ClientLoanDTO> getClientLoansADMIN() {
        return clientLoanRepository.findAll().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toList());
    }

    @GetMapping("/admin/clientloans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable Long id){
        return clientLoanRepository.findById(id).map(clientLoan -> new ClientLoanDTO(clientLoan)).orElse(null);
    }

    @Transactional
    @PostMapping("/clients/current/pay-with-account")
    public ResponseEntity<Object> payLoanAccount(
            @RequestBody PayLoanAccountDTO payLoan,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Account originAccount = accountRepository.findByNumber(payLoan.getNumber());
        AccountCard originCard = accountCardRepository.findByAccount(originAccount);

        ClientLoan loanRequested = clientLoanRepository.findById(payLoan.getId()).orElse(null);

        if(payLoan.getNumber().isEmpty() || String.valueOf(payLoan.getMonthlyPayment()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getNumber() == null){
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account -> account.getNumber().contains(originAccount.getNumber())).collect(Collectors.toSet());
        if(clientAccount.size() == 0){
            return new ResponseEntity<>("Account not associated with the client.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getBalance() < payLoan.getMonthlyPayment()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payLoan.getMonthlyPayment() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payLoan.getMonthlyPayment() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(loanRequested.getAmount() <= 0){
            return new ResponseEntity<>("Paid loan.", HttpStatus.FORBIDDEN);
        }

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        originAccount.setBalance(originAccount.getBalance()- payLoan.getMonthlyPayment());
        originCard.setBalance(originCard.getBalance() - payLoan.getMonthlyPayment());

        loanRequested.setAmount(loanRequested.getAmount()- payLoan.getMonthlyPayment());
        loanRequested.setPayments(loanRequested.getPayments()- payLoan.getPayments());

        transactionRepository.save(new Transaction(originAccount, originAccount.getHolder(), originAccount.getNumber(), TransactionType.Payment, payLoan.getMonthlyPayment(),"Payment"+" "+loanRequested.getName()+" "+"loan", "P"+numberDescription, "Mindhub Brothers Bank, Inc.", null, originAccount.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/pay-with-debit-card")
    public ResponseEntity<Object> payLoanDebitCard(
            @RequestBody PayLoanCardDTO payLoanCard,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Card card = cardRepository.findByNumber(payLoanCard.getNumber());

        if(payLoanCard.getNumber().isEmpty() || payLoanCard.getDate().isEmpty() || String.valueOf(payLoanCard.getCvv()).isEmpty() || String.valueOf(payLoanCard.getMonthlyPayment()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(card == null){
            return new ResponseEntity<>("This card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getCvv() != card.getCvv()){
            return new ResponseEntity<>("Invalid CVV.", HttpStatus.FORBIDDEN);
        }

        AccountCard originCard = accountCardRepository.findByCard(card);
        Account originAccount = accountRepository.findByNumber(originCard.getAccount().getNumber());

        ClientLoan loanRequested = clientLoanRepository.findById(payLoanCard.getId()).orElse(null);

        if(originCard.getBalance() < payLoanCard.getMonthlyPayment()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getMonthlyPayment() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getMonthlyPayment() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(loanRequested.getAmount() <= 0){
            return new ResponseEntity<>("Paid loan.", HttpStatus.FORBIDDEN);
        }

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        originAccount.setBalance(originAccount.getBalance() - payLoanCard.getMonthlyPayment());
        originCard.setBalance(originCard.getBalance() - payLoanCard.getMonthlyPayment());

        loanRequested.setAmount(loanRequested.getAmount() - payLoanCard.getMonthlyPayment());
        loanRequested.setPayments(loanRequested.getPayments() - payLoanCard.getPayments());

        transactionRepository.save(new Transaction(originAccount, originAccount.getHolder(), originAccount.getNumber(), TransactionType.Payment, payLoanCard.getMonthlyPayment(),"Payment"+" "+loanRequested.getName()+" "+"loan", "P"+numberDescription, "Mindhub Brothers Bank, Inc.", null, originAccount.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/pay-with-credit-card")
    public ResponseEntity<Object> payLoanCreditCard(
            @RequestBody PayLoanCardDTO payLoanCard,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        ClientLoan loanRequested = clientLoanRepository.findById(payLoanCard.getId()).orElse(null);
        CreditCard creditCard = creditCardRepository.findByNumber(payLoanCard.getNumber());

        if(payLoanCard.getNumber().isEmpty() || payLoanCard.getDate().isEmpty() || String.valueOf(payLoanCard.getCvv()).isEmpty() || String.valueOf(payLoanCard.getMonthlyPayment()).isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(creditCard == null){
            return new ResponseEntity<>("This card does not exist.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getCvv() != creditCard.getCvv()){
            return new ResponseEntity<>("Invalid CVV.", HttpStatus.FORBIDDEN);
        }

        if(creditCard.getAmountLimit() < payLoanCard.getMonthlyPayment()){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getMonthlyPayment() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(payLoanCard.getMonthlyPayment() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(loanRequested.getAmount() <= 0){
            return new ResponseEntity<>("Paid loan.", HttpStatus.FORBIDDEN);
        }

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        creditCard.setAmountLimit(creditCard.getAmountLimit() - payLoanCard.getMonthlyPayment());
        creditCard.setBalance(creditCard.getBalance() + payLoanCard.getMonthlyPayment());

        loanRequested.setAmount(loanRequested.getAmount() - payLoanCard.getMonthlyPayment());
        loanRequested.setPayments(loanRequested.getPayments() - payLoanCard.getPayments());

        transactionCardRepository.save(new TransactionCard(creditCard, TransactionType.Debit, "Payment"+" "+loanRequested.getName()+" "+"loan", "P"+numberDescription, creditCard.getAmountLimit(), creditCard.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clients/current/clientloans/{id}")
    public ResponseEntity<Object> deleteLoans(
            @PathVariable Long id){

        ClientLoan clientLoan = clientLoanRepository.findById(id).orElse(null);

        if(clientLoan.getAmount() > 0){
            return new ResponseEntity<>("To delete this loan, first you should pay it.", HttpStatus.FORBIDDEN);
        }

        clientLoanRepository.deleteById(clientLoan.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
