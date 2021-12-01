package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "creditCard", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<TransactionCard> transactionCards = new HashSet<>();

    //CONSTRUCTOR
    public CreditCard() { }

    public CreditCard(Client client, CardType type, CardColor color, String number, int cvv, double amountLimit, double balance, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.type = type;
        this.color = color;
        this.client = client;
        this.cardholder = client.getFirstName()+" "+client.getLastName();
        this.number = number;
        this.cvv = cvv;
        this.amountLimit = amountLimit;
        this.balance = balance;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
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
    public Set<TransactionCard> getTransactionCards() {return transactionCards;}

    @JsonIgnore
    public Client getClient() {return client;}

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
    public void setClient(Client client) {this.client = client;}
}
