package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class AccountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String numberAccount;
    private String numberCard;
    private int cvv;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private Card card;

    //CONSTRUCTOR
    public AccountCard() { }

    public AccountCard(Account account, Card card, String numberAccount, String numberCard, int cvv, double balance) {
        this.account = account;
        this.card = card;
        this.numberAccount = account.getNumber();
        this.numberCard = card.getNumber();
        this.cvv = card.getCvv();
        this.balance = account.getBalance();
    }

    //GETTER
    public Long getId() {return id;}
    public String getNumberAccount() {return numberAccount;}
    public String getNumberCard() {return numberCard;}
    public int getCvv() {return cvv;}
    public double getBalance() {return balance;}

    @JsonIgnore
    public Account getAccount() {return account;}

    @JsonIgnore
    public Card getCard() {return card;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setNumberAccount(String numberAccount) {this.numberAccount = numberAccount;}
    public void setNumberCard(String numberCard) {this.numberCard = numberCard;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setBalance(double balance) {this.balance = balance;}
    public void setAccount(Account account) {this.account = account;}
    public void setCard(Card card) {this.card = card;}
}
