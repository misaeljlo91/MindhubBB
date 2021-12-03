package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountCardRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PDFService pdfService;

    @GetMapping("/admin/transactions")
    public List<TransactionDTO> getListTransactions() {
        return transactionRepository.findAll().stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());
    }

    @GetMapping("/admin/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id){
        return transactionRepository.findById(id).map(transaction -> new TransactionDTO(transaction)).orElse(null);
    }

    @GetMapping("/clients/current/transactions/{id}")
    public TransactionDTO getTransactionClient(
            @PathVariable Long id,
            Authentication authentication){

        Client signClient = clientRepository.findByEmail(authentication.getName());

        return transactionRepository.findById(id).map(transaction -> new TransactionDTO(transaction)).orElse(null);
    }

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<Object> createTransactions(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        Account originAccount = accountRepository.findByNumber(origin);
        Account destinationAccount = accountRepository.findByNumber(destination);

        AccountCard originCard = accountCardRepository.findByAccount(originAccount);
        AccountCard destinationCard = accountCardRepository.findByAccount(destinationAccount);

        if(origin.isEmpty() || destination.isEmpty() || String.valueOf(amount).isEmpty() || description.isEmpty()) {
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getNumber() == null || destinationAccount.getNumber() == null){
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account -> account.getNumber().contains(originAccount.getNumber())).collect(Collectors.toSet());
        if(clientAccount.size() == 0){
            return new ResponseEntity<>("Account not associated with the client.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getNumber() == destinationAccount.getNumber()){
            return new ResponseEntity<>("The accounts cannot be equals.", HttpStatus.FORBIDDEN);
        }

        if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }

        if(amount == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        originAccount.setBalance(originAccount.getBalance()-amount);
        originCard.setBalance(originCard.getBalance()-amount);

        destinationAccount.setBalance(destinationAccount.getBalance()+amount);
        destinationCard.setBalance(destinationCard.getBalance()+amount);

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        transactionRepository.save(new Transaction(originAccount, originAccount.getHolder(), originAccount.getNumber(), TransactionType.Debit, amount, description,"D"+numberDescription,  destinationAccount.getHolder(),  destinationAccount.getNumber(), originAccount.getBalance(), LocalDateTime.now()));
        transactionRepository.save(new Transaction(destinationAccount, destinationAccount.getHolder(), destinationAccount.getNumber(), TransactionType.Credit, amount, description,"C"+numberDescription,  originAccount.getHolder(),  originAccount.getNumber(), destinationAccount.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/transaction/pdf")
    public ResponseEntity<?> transactionPDF(
            HttpServletResponse httpServletResponse,
            @RequestParam String numberDescription,
            @RequestParam String client1,
            @RequestParam String account1,
            @RequestParam String client2,
            @RequestParam String account2,
            @RequestParam String type,
            @RequestParam double amount,
            @RequestParam String description,
            Authentication authentication) throws IOException {

        Client signClient = clientRepository.findByEmail(authentication.getName());

        httpServletResponse.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attacment; filename=Transaction_"+"N°_"+numberDescription+".pdf";
        httpServletResponse.setHeader(headerKey, headerValue);


        this.pdfService.export(httpServletResponse, numberDescription, client1, account1, client2, account2, type, amount, description);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
