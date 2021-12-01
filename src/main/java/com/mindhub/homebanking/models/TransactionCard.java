package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TransactionCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    private String description;
    private String numberDescription;
    private double amountLimit;
    private double balance;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creditCard_id")
    private CreditCard creditCard;

    //CONSTRUCTOR
    public TransactionCard() { }

    public TransactionCard(CreditCard creditCard, TransactionType type, String description, String numberDescription, double amountLimit, double balance, LocalDateTime creationDate) {
        this.creditCard = creditCard;
        this.type = type;
        this.description = description;
        this.numberDescription = numberDescription;
        this.amountLimit = amountLimit;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    //GETTER
    public Long getId() {return id;}
    public TransactionType getType() {return type;}
    public String getDescription() {return description;}
    public String getNumberDescription() {return numberDescription;}
    public double getAmountLimit() {return amountLimit;}
    public double getBalance() {return balance;}
    public LocalDateTime getCreationDate() {return creationDate;}

    @JsonIgnore
    public CreditCard getCreditCard() {return creditCard;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setType(TransactionType type) {this.type = type;}
    public void setDescription(String description) {this.description = description;}
    public void setNumberDescription(String numberDescription) {this.numberDescription = numberDescription;}
    public void setAmountLimit(double amountLimit) {this.amountLimit = amountLimit;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public void setCreditCard(CreditCard creditCard) {this.creditCard = creditCard;}
}
