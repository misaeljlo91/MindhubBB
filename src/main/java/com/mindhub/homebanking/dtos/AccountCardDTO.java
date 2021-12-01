package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.AccountCard;

public class AccountCardDTO {

    private Long id;
    private Long accountID;
    private String numberAccount;
    private String numberCard;
    private int cvv;
    private double balance;

    //CONSTRUCTOR
    public AccountCardDTO() { }

    public AccountCardDTO(AccountCard accountCard) {
        this.id = accountCard.getId();
        this.accountID = accountCard.getAccount().getId();
        this.numberAccount = accountCard.getNumberAccount();
        this.numberCard = accountCard.getNumberCard();
        this.cvv = accountCard.getCvv();
        this.balance = accountCard.getBalance();
    }

    //GETTER
    public Long getId() {return id;}
    public Long getAccountID() {return accountID;}
    public String getNumberAccount() {return numberAccount;}
    public String getNumberCard() {return numberCard;}
    public int getCvv() {return cvv;}
    public double getBalance() {return balance;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setAccountID(Long accountID) {this.accountID = accountID;}
    public void setNumberAccount(String numberAccount) {this.numberAccount = numberAccount;}
    public void setNumberCard(String numberCard) {this.numberCard = numberCard;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setBalance(double balance) {this.balance = balance;}
}
