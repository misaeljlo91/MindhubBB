package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;
    private Long loanID;
    private String name;
    private double amount;
    private Integer payments;
    private double monthlyPayment;

    //CONSTRUCTOR
    public ClientLoanDTO() { }

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.loanID = clientLoan.getLoan().getId();
        this.name = clientLoan.getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.monthlyPayment = clientLoan.getMonthlyPayment();
    }

    //GETTER
    public Long getId() {return id;}
    public Long getLoanID() {return loanID;}
    public String getName() {return name;}
    public double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public double getMonthlyPayment() {return monthlyPayment;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setLoanID(Long loanID) {this.loanID = loanID;}
    public void setName(String name) {this.name = name;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setPayments(Integer payments) {this.payments = payments;}
    public void setMonthlyPayment(double monthlyPayment) {this.monthlyPayment = monthlyPayment;}
}
