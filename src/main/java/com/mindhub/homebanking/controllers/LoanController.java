package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
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
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountCardRepository accountCardRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/admin/loans")
    public List<LoanDTO> getListLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @GetMapping("/clients/current/loans")
    public List<LoanDTO> getListLoansClient() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @PostMapping("/admin/loans")
    public ResponseEntity<Object> addLoan(
            @RequestParam String name,
            @RequestParam double maxAmount,
            @RequestParam int averageInterest,
            @RequestParam Integer payment1,
            @RequestParam Integer payment2,
            @RequestParam Integer payment3) {

        if(name.isEmpty() || String.valueOf(averageInterest).isEmpty() || String.valueOf(maxAmount).isEmpty() || payment1 == null || payment2 == null || payment3 == null){
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(maxAmount == 0){
            return new ResponseEntity<>("The maximum amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(averageInterest == 0){
            return new ResponseEntity<>("The average interest cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        loanRepository.save(new Loan(name, maxAmount, averageInterest, List.of(payment1, payment2, payment3)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/admin/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id){
        return loanRepository.findById(id).map(loan -> new LoanDTO(loan)).orElse(null);
    }

    @PutMapping("/admin/loans/{id}")
    public ResponseEntity<Object> changeLoan(
            @RequestParam String name,
            @RequestParam double maxAmount,
            @RequestParam int averageInterest,
            @PathVariable Long id) {

        Loan loan = loanRepository.findById(id).orElse(null);

        if(name.isEmpty() || String.valueOf(maxAmount).isEmpty()){
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(maxAmount == 0){
            return new ResponseEntity<>("The maximum amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(averageInterest == 0){
            return new ResponseEntity<>("The average interest cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        loan.setName(name);
        loan.setMaxAmount(maxAmount);
        loan.setAverageInterest(averageInterest);

        loanRepository.save(loan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/loans/{id}")
    public ResponseEntity<Object> deleteLoan(
            @PathVariable Long id) {

        Loan loan = loanRepository.findById(id).orElse(null);

        loanRepository.deleteById(loan.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/loans")
    public ResponseEntity<Object> requestLoan(
            @RequestBody LoanApplicationDTO loan,
            Authentication authentication) {

        Client signClient = clientRepository.findByEmail(authentication.getName());
        Account accountClient = accountRepository.findByNumber(loan.getAccount());
        Loan loanRequest = loanRepository.findByName(loan.getName());
        AccountCard cardClient = accountCardRepository.findByAccount(accountClient);

        if(loan.getName().isEmpty() || loan.getPayments() == null || String.valueOf(loan.getAmount()).isEmpty() || loan.getAccount().isEmpty()){
            return new ResponseEntity<>("The fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if(loan.getAmount() == 0){
            return new ResponseEntity<>("The amount cannot be equal to 0.", HttpStatus.FORBIDDEN);
        }

        if(loan.getAmount() < 0){
            return new ResponseEntity<>("The amount cannot be negative.", HttpStatus.FORBIDDEN);
        }

        if(loan.getAmount() > loanRequest.getMaxAmount()){
            return new ResponseEntity<>("The amount requested exceeds the maximum amount.", HttpStatus.FORBIDDEN);
        }

        Set<Account> clientAccount = signClient.getAccounts().stream().filter(account -> account.getNumber().equals(accountClient.getNumber())).collect(Collectors.toSet());
        if(clientAccount.size() == 0){
            return new ResponseEntity<>("Account not associated with the client.", HttpStatus.FORBIDDEN);
        }

        if(accountClient == null){
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }

        Set<ClientLoan> loanClient = signClient.getClientLoans().stream().filter(loan1 -> loan1.getLoan().getName().equals(loan.getName())).collect(Collectors.toSet());
        if(loanClient.size() >= 1){
            return new ResponseEntity<>("You already have a loan of this type.", HttpStatus.FORBIDDEN);
        }

        if(signClient.getClientLoans().size() >= 3){
            return new ResponseEntity<>("Maximum loans allowed", HttpStatus.FORBIDDEN);
        }

        double amountPay = loan.getAmount() + ((loan.getAmount() * loanRequest.getAverageInterest())/100);

        double monthlyPayment = amountPay/loan.getPayments();

        int nroDescription = (int)((Math.random()*(999999-100000+1))+100000);
        int idDescription = (int)((Math.random()*(999-100+1))+100);
        String numberDescription = nroDescription+"/"+idDescription;

        accountClient.setBalance(accountClient.getBalance()+loan.getAmount());
        cardClient.setBalance(cardClient.getBalance()+loan.getAmount());

        clientLoanRepository.save(new ClientLoan(signClient, loanRequest, loanRequest.getName(), amountPay, loan.getPayments(), monthlyPayment));
        transactionRepository.save(new Transaction(accountClient, accountClient.getNumber(), TransactionType.Loan, loan.getAmount(), loan.getName()+" "+"loan approved", "L"+numberDescription, "Mindhub Brothers Bank, Inc.", null, accountClient.getBalance(), LocalDateTime.now()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
