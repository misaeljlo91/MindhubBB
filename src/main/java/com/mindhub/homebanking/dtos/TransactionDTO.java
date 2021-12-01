package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private String account1;
    private TransactionType type;
    private double amount;
    private String description;
    private String numberDescription;
    private String holder2;
    private String account2;
    private double balanceAccount;
    private LocalDateTime creationDate;
    private Account account;

    //CONSTRUCTOR
    public TransactionDTO() { }

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.account1 = transaction.getAccount1();
        this.account = transaction.getAccount();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.numberDescription = transaction.getNumberDescription();
        this.holder2 = transaction.getHolder2();
        this.account2 = transaction.getAccount2();
        this.balanceAccount = transaction.getBalanceAccount();
        this.creationDate = transaction.getCreationDate();
    }

    //GETTER
    public Long getId() {return id;}
    public String getAccount1() {return account1;}
    public TransactionType getType() {return type;}
    public double getAmount() {return amount;}
    public String getDescription() {return description;}
    public String getNumberDescription() {return numberDescription;}
    public String getHolder2() {return holder2;}
    public String getAccount2() {return account2;}
    public double getBalanceAccount() {return balanceAccount;}
    public LocalDateTime getCreationDate() {return creationDate;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setAccount1(String account1) {this.account1 = account1;}
    public void setType(TransactionType type) {this.type = type;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setDescription(String description) {this.description = description;}
    public void setNumberDescription(String numberDescription) {this.numberDescription = numberDescription;}
    public void setHolder2(String holder2) {this.holder2 = holder2;}
    public void setAccount2(String account2) {this.account2 = account2;}
    public void setBalanceAccount(double balanceAccount) {this.balanceAccount = balanceAccount;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
}
