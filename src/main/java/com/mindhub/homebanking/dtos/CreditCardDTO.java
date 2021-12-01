package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.CreditCard;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class CreditCardDTO {

    private Long id;
    private CardType type;
    private CardColor color;
    private String cardholder;
    private String number;
    private int cvv;
    private double amountLimit;
    private double balance;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private Set<TransactionCardDTO> transactionCards;

    //CONSTRUCTOR
    public CreditCardDTO() { }

    public CreditCardDTO(CreditCard card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.cardholder = card.getCardholder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.amountLimit = card.getAmountLimit();
        this.balance = card.getBalance();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.transactionCards = card.getTransactionCards().stream().map(TransactionCardDTO::new).collect(Collectors.toSet());
    }

    //GETTER
    public Long getId() {return id;}
    public CardType getType() {return type;}
    public CardColor getColor() {return color;}
    public String getCardholder() {return cardholder;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public double getAmountLimit() {return amountLimit;}
    public double getBalance() {return balance;}
    public LocalDateTime getFromDate() {return fromDate;}
    public LocalDateTime getThruDate() {return thruDate;}
    public Set<TransactionCardDTO> getTransactionCards() {return transactionCards;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setType(CardType type) {this.type = type;}
    public void setColor(CardColor color) {this.color = color;}
    public void setCardholder(String cardholder) {this.cardholder = cardholder;}
    public void setNumber(String number) {this.number = number;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setAmountLimit(double amountLimit) {this.amountLimit = amountLimit;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}
    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}
}
