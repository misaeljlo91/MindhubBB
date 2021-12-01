package com.mindhub.homebanking.dtos;

public class PayCreditCardCardDTO {

    private String number;
    private String date;
    private int cvv;
    private Long id;
    private double amount;

    //CONSTRUCTOR
    public PayCreditCardCardDTO() { }

    public PayCreditCardCardDTO(String number, String date, int cvv, Long id, double amount) {
        this.number = number;
        this.date = date;
        this.cvv = cvv;
        this.id = id;
        this.amount = amount;
    }

    //GETTER
    public String getNumber() {return number;}
    public String getDate() {return date;}
    public int getCvv() {return cvv;}
    public Long getId() {return id;}
    public double getAmount() {return amount;}

    //SETTER
    public void setNumber(String number) {this.number = number;}
    public void setDate(String date) {this.date = date;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setId(Long id) {this.id = id;}
    public void setAmount(double amount) {this.amount = amount;}
}
