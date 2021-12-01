package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {

    private Long id;
    private String name;
    private double maxAmount;
    private int averageInterest;
    private List<Integer> payments;

    //CONSTRUCTOR
    public LoanDTO() { }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.averageInterest = loan.getAverageInterest();
        this.payments = loan.getPayments();
    }

    //GETTER
    public Long getId() {return id;}
    public String getName() {return name;}
    public double getMaxAmount() {return maxAmount;}
    public int getAverageInterest() {return averageInterest;}
    public List<Integer> getPayments() {return payments;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setMaxAmount(double maxAmount) {this.maxAmount = maxAmount;}
    public void setAverageInterest(int averageInterest) {this.averageInterest = averageInterest;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}
}
