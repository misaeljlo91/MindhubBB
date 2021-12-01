package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CreditCard;
import com.mindhub.homebanking.models.TransactionCard;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionCardDTO {

    private Long id;
    private TransactionType type;
    private String description;
    private String numberDescription;
    private double amountLimit;
    private double balance;
    private LocalDateTime creationDate;
    private CreditCard creditCard;

    //CONSTRUCTOR
    public TransactionCardDTO() { }

    public TransactionCardDTO(TransactionCard transactionCard) {
        this.creditCard = transactionCard.getCreditCard();
        this.id = transactionCard.getId();
        this.type = transactionCard.getType();
        this.description = transactionCard.getDescription();
        this.numberDescription = transactionCard.getNumberDescription();
        this.amountLimit = transactionCard.getAmountLimit();
        this.balance = transactionCard.getBalance();
        this.creationDate = transactionCard.getCreationDate();
    }

    //GETTER
    public Long getId() {return id;}
    public TransactionType getType() {return type;}
    public String getDescription() {return description;}
    public String getNumberDescription() {return numberDescription;}
    public double getAmountLimit() {return amountLimit;}
    public double getBalance() {return balance;}
    public LocalDateTime getCreationDate() {return creationDate;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setType(TransactionType type) {this.type = type;}
    public void setDescription(String description) {this.description = description;}
    public void setNumberDescription(String numberDescription) {this.numberDescription = numberDescription;}
    public void setAmountLimit(double amountLimit) {this.amountLimit = amountLimit;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
}
