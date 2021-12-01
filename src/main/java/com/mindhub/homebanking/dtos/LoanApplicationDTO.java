package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private String name;
    private Integer payments;
    private double amount;
    private String account;

    //CONSTRUCTOR
    public LoanApplicationDTO() { }

    public LoanApplicationDTO(String name, Integer payments, double amount, String account) {
        this.name = name;
        this.payments = payments;
        this.amount = amount;
        this.account = account;
    }

    //GETTER
    public String getName() {return name;}
    public Integer getPayments() {return payments;}
    public double getAmount() {return amount;}
    public String getAccount() {return account;}

    //SETTER
    public void setName(String name) {this.name = name;}
    public void setPayments(Integer payments) {this.payments = payments;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setAccount(String account) {this.account = account;}
}
