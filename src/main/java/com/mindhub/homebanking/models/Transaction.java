package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private Long id;
        private String holder1;
        private String account1;
        private TransactionType type;
        private double amount;
        private String description;
        private String numberDescription;
        private String holder2;
        private String account2;
        private double balanceAccount;
        private LocalDateTime creationDate;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "account_id")
        private Account account;

    //CONSTRUCTOR
    public Transaction() { }

    public Transaction(Account account, String holder1, String account1, TransactionType type, double amount, String description, String numberDescription, String holder2, String account2, double balanceAccount, LocalDateTime creationDate) {
        this.account = account;
        this.holder1 = holder1;
        this.account1 = account1;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.numberDescription = numberDescription;
        this.holder2 = holder2;
        this.account2 = account2;
        this.balanceAccount = balanceAccount;
        this.creationDate = creationDate;
    }

    //GETTER
    public Long getId() {return id;}
    public String getHolder1() {return holder1;}
    public String getAccount1() {return account1;}
    public TransactionType getType() {return type;}
    public double getAmount() {return amount;}
    public String getDescription() {return description;}
    public String getNumberDescription() {return numberDescription;}
    public String getHolder2() {return holder2;}
    public String getAccount2() {return account2;}
    public double getBalanceAccount() {return balanceAccount;}
    public LocalDateTime getCreationDate() {return creationDate;}

    @JsonIgnore
    public Account getAccount() {return account;}


    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setHolder1(String holder1) {this.holder1 = holder1;}
    public void setAccount1(String account1) {this.account1 = account1;}
    public void setType(TransactionType type) {this.type = type;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setDescription(String description) {this.description = description;}
    public void setNumberDescription(String numberDescription) {this.numberDescription = numberDescription;}
    public void setHolder2(String holder2) {this.holder2 = holder2;}
    public void setAccount2(String account2) {this.account2 = account2;}
    public void setBalanceAccount(double balanceAccount) {this.balanceAccount = balanceAccount;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public void setAccount(Account account) {this.account = account;}
}
