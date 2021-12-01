package com.mindhub.homebanking.dtos;

public class PayLoanCardDTO {

    private String number;
    private String date;
    private int cvv;
    private Long id;
    private String name;
    private Integer payments;
    private double monthlyPayment;

    //CONSTRUCTOR
    public PayLoanCardDTO() { }

    public PayLoanCardDTO(String number, String date, int cvv, Long id, String name, Integer payments, double monthlyPayment) {
        this.number = number;
        this.date = date;
        this.cvv = cvv;
        this.id = id;
        this.name = name;
        this.payments = payments;
        this.monthlyPayment = monthlyPayment;
    }

    //GETTER
    public String getNumber() {return number;}
    public String getDate() {return date;}
    public int getCvv() {return cvv;}
    public Long getId() {return id;}
    public String getName() {return name;}
    public Integer getPayments() {return payments;}
    public double getMonthlyPayment() {return monthlyPayment;}

    //SETTER
    public void setNumber(String number) {this.number = number;}
    public void setDate(String date) {this.date = date;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPayments(Integer payments) {this.payments = payments;}
    public void setMonthlyPayment(double monthlyPayment) {this.monthlyPayment = monthlyPayment;}
}
