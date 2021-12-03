package com.mindhub.homebanking.dtos;

public class PayLoanAccountDTO {

    private String number;
    private Long id;
    //private String name;
    private Integer payments;
    private double monthlyPayment;

    //CONSTRUCTOR
    public PayLoanAccountDTO() { }

    public PayLoanAccountDTO(String number, Long id, Integer payments, double monthlyPayment) {
        this.number = number;
        this.id = id;
        //this.name = name;
        this.payments = payments;
        this.monthlyPayment = monthlyPayment;
    }

    //GETTER
    public String getNumber() {return number;}
    public Long getId() {return id;}
    //public String getName() {return name;}
    public Integer getPayments() {return payments;}
    public double getMonthlyPayment() {return monthlyPayment;}

    //SETTER
    public void setNumber(String number) {this.number = number;}
    public void setId(Long id) {this.id = id;}
    //public void setName(String name) {this.name = name;}
    public void setPayments(Integer payments) {this.payments = payments;}
    public void setMonthlyPayment(double monthlyPayment) {this.monthlyPayment = monthlyPayment;}
}
